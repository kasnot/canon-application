package io.pikei.canon;

import io.pikei.canon.framework.api.constant.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.blackdread.camerabinding.jna.EdsdkLibrary;
import io.pikei.canon.framework.api.camera.CameraManager;
import io.pikei.canon.framework.api.camera.CanonCamera;
import io.pikei.canon.framework.api.command.GetPropertyCommand;
import io.pikei.canon.framework.api.command.builder.*;
import io.pikei.canon.framework.api.helper.factory.CanonFactory;
import io.pikei.canon.framework.api.helper.logic.LiveViewLogic;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Singleton Canon Camera Manager handling the lifecycle of the camera.
 */
@Component
@Slf4j
public class CanonCameraManager {

    private static final int WAIT_TIMEOUT_THRESHOLD = 20000; //TODO Configuration.
    private static final String IMAGE_FORMAT = "jpeg"; //TODO Configuration.
    private static final String OUTPUT_IMAGE_DIRECTORY = "output/image-data.jpg"; //TODO Configuration.

    private static final int WAIT_FOR_RECORD_EVENT_INTERVAL = 250;
    private static final int WAIT_FOR_LIVE_VIEW_INTERVAL = 500;
    private static final int LIVE_VIEW_INTERVAL = 250;
    private static final int WAIT_FOR_SHOOT_INTERVAL = 100;

    private final CanonConfiguration canonConfig;

    @Getter
    private CameraMetadata metadata;
    private CanonCamera cameraInstance;
    private EdsdkLibrary.EdsCameraRef cameraRef;
    private final AtomicBoolean recordEvent = new AtomicBoolean(false);
    private final AtomicBoolean liveView = new AtomicBoolean(false);
    private final AtomicBoolean takeShoot = new AtomicBoolean(false);

    /**
     *
     */
    public CanonCameraManager(final CanonConfiguration canonConfig){
        log.info("Starting up Canon Camera Manager");
        this.canonConfig = canonConfig;
        final List<CanonCamera> availableCameras = CameraManager.getAllConnected();
        log.info("Total connected cameras found {}", availableCameras.size());
        availableCameras.forEach(camera -> {
            log.info("Camera Serial Number {}", camera.getSerialNumber());
        });
        CameraManager.getCameraBySerialNumber(canonConfig.getSerialNumber())
                .ifPresent(camera -> {
                    log.info("Camera with serial [{}]: found.", canonConfig.getSerialNumber());
                    initializeCamera(camera);
                });

        CanonFactory.cameraAddedEventLogic().addCameraAddedListener(event -> {
            log.info("Camera Added: {}", event);
            CameraManager.getCameraBySerialNumber(canonConfig.getSerialNumber())
                    .ifPresent(camera -> {
                        log.info("Camera with serial [{}]: found.", canonConfig.getSerialNumber());
                        initializeCamera(camera);
                    });
        });

    }

    /**
     * Initialize a specific camera instance.
     * @param camera
     */
    private void initializeCamera(@NonNull final CanonCamera camera) {
        log.info("Initializing camera {} ", camera.getSerialNumber().orElse("N/A"));
        cameraInstance = camera;
        cameraRef = cameraInstance.getCameraRef().orElseThrow(() -> new RuntimeException("No camera reference found."));
        CanonCommandFactory.get(cameraInstance.getEvent().registerObjectEventCommand());
        CanonCommandFactory.get(cameraInstance.getEvent().registerPropertyEventCommand());
        CanonCommandFactory.get(cameraInstance.getEvent().registerStateEventCommand());
        final EdsImageQuality imageQuality = CanonCommandFactory.getCameraProperty(cameraInstance, new GetPropertyCommand.ImageQuality());
        final EdsBatteryLevel2 batteryLevel2 = CanonCommandFactory.getCameraProperty(cameraInstance, new GetPropertyCommand.BatteryLevel());
        metadata = new CameraMetadata(
                CanonCommandFactory.getCameraProperty(cameraInstance, new GetPropertyCommand.ProductName()),
                (imageQuality != null) ? imageQuality.description() : null,
                CanonCommandFactory.getCameraProperty(cameraInstance, new GetPropertyCommand.FirmwareVersion()),
                CanonCommandFactory.getCameraProperty(cameraInstance, new GetPropertyCommand.SerialNumber()),
                (batteryLevel2 != null) ? batteryLevel2.description() : null
        );
    }

    public void open() {

        log.info("Opening camera for live view...");
        if( liveView.get() ){
            log.info("Camera still recording...returning");
            return;
        }
        liveView.set(true);

        CanonFactory.cameraObjectEventLogic().addCameraObjectListener(cameraRef, event -> {
            log.info("Camera Object: {}", event.toString());
        });

        CanonFactory.cameraPropertyEventLogic().addCameraPropertyListener(cameraRef, event -> {
            log.info("[INFO]Record event received, {}", event);
            if( event.getPropertyEvent() == EdsPropertyEvent.kEdsPropertyEvent_PropertyChanged
                    && event.getPropertyId() == EdsPropertyID.kEdsPropID_Record
            ){
                recordEvent.set(true);
                log.info("Record event received, {}", event);
                log.info("Starting taking live view...");
            }
        });

        CanonFactory.cameraStateEventLogic().addCameraStateListener(cameraRef, event -> {
            log.info(event.toString());
        });

        cameraInstance.openSession();
        log.info("Camera session opened successfully!!!");
        cameraInstance.getLiveView().beginLiveViewAsync();

        if( waitForRecordEvent() != WaitStatus.SUCCESS ){
            log.error("Unable to fetch record event, returning.");
            closeInternal();
            return;
        }

        if( waitForLiveView() != WaitStatus.SUCCESS ){
            log.error("Unable to enable camera live view, returning.");
            closeInternal();
            return;
        }

        final LiveViewLogic liveViewLogic = CanonFactory.liveViewLogic();
        while( liveView.get() ){
            log.info("Live view feedback.. {}", liveView.get());
            CompletableFuture.runAsync(() -> {
                try {
                    takeShoot.set(true);
                    ImageIO.write(liveViewLogic.getLiveViewImage(cameraRef), IMAGE_FORMAT, new File(OUTPUT_IMAGE_DIRECTORY));
                    takeShoot.set(false);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            });
            try {
                Thread.sleep(LIVE_VIEW_INTERVAL);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("Stopping live view feedback.. {}", liveView.get());
        closeInternal();
    }

    /**
     *  Method for waiting for record event from camera events.
     *  You must have register event listener for this method to work.
     *  @return - Status from the wait..
     */
    private WaitStatus waitForRecordEvent() {
        int waitRest = WAIT_TIMEOUT_THRESHOLD;
        while(!recordEvent.get()){
            log.info("Waiting for live view to be ready.");
            try {
                Thread.sleep(WAIT_FOR_RECORD_EVENT_INTERVAL);
                waitRest -= WAIT_FOR_RECORD_EVENT_INTERVAL;
                if( waitRest < 0 ) return WaitStatus.TIMEOUT;
            } catch (InterruptedException e) {
                return WaitStatus.FAILED;
            }
        }
        return WaitStatus.SUCCESS;
    }

    /**
     *  Method for waiting for live view to become available.
     *  @return - Status from the weight.
     */
    private WaitStatus waitForLiveView() {
        int waitRest = WAIT_TIMEOUT_THRESHOLD;
        while (!CanonCommandFactory.get(cameraInstance.getLiveView().isLiveViewActiveAsync())) {
            log.info("Waiting for live view to be ready..");
            try {
                Thread.sleep(WAIT_FOR_LIVE_VIEW_INTERVAL);
                waitRest -= WAIT_FOR_LIVE_VIEW_INTERVAL;
                if( waitRest < 0 ) return WaitStatus.TIMEOUT;
            } catch (InterruptedException e) {
                return WaitStatus.FAILED;
            }
        }
        return WaitStatus.SUCCESS;
    }

    /**
     *  Method for waiting for shoot event to finish.
     *  @return - Status from the weight.
     */
    private void waitForShootEvent() throws InterruptedException {
        while(takeShoot.get()){
            log.info("Waiting for live shoot to be completed..");
            Thread.sleep(WAIT_FOR_SHOOT_INTERVAL);
        }
    }

    /**
     * Stop/Close camera session.
     */
    public void close(){
        liveView.set(false);
        recordEvent.set(false);
    }

    /**
     * Camera Session clean method.
     */
    private void closeInternal(){
        log.info("Going to close live view camera session..");
        try {
            waitForShootEvent();
            cameraInstance.getLiveView().endLiveViewAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Unable to close live view.", e);
        }
        log.info("Camera live view closed successfully!!!");
        cameraInstance.closeSession();
        log.info("Camera session closed successfully!!!");
        liveView.set(false);
        recordEvent.set(false);
        CanonFactory.cameraObjectEventLogic().unregisterCameraObjectEvent(cameraRef);
        CanonFactory.cameraPropertyEventLogic().unregisterCameraPropertyEvent(cameraRef);
        CanonFactory.cameraStateEventLogic().unregisterCameraStateEvent(cameraRef);
        log.info("Camera listeners clear, success!!!");
    }

}
