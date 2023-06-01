package io.pikei.canon;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import io.pikei.canon.framework.api.camera.CanonCamera;
import io.pikei.canon.framework.api.command.CanonCommand;
import io.pikei.canon.framework.api.command.GetPropertyCommand;
import io.pikei.canon.framework.api.helper.factory.CanonFactory;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

/**
 * Factory method for generating command for Canon
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CanonCommandFactory {

    /**
     *
     * @param command
     * @return
     * @param <R>
     */
    public static <R> R get(final CanonCommand<R> command) {
        try {
            return command.get();
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Unable to generate command {}", command);
            throw new IllegalStateException(e);
        }
    }

    /**
     * Get camera property.
     * @param cameraInstance
     * @param command
     * @return
     * @param <R>
     */
    public static <R> R getCameraProperty(@NonNull final CanonCamera cameraInstance,
                                          @NonNull final GetPropertyCommand<R> command) {
        try {
            command.setTargetRef(cameraInstance.getCameraRef().get());
            command.setTimeout(Duration.ofMillis(2000));
            CanonFactory.commandDispatcher().scheduleCommand(command);
            return command.get();
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Unable to generate command {}", command);
            return null;
        }
    }
}
