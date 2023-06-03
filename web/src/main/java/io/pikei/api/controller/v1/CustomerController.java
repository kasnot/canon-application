package io.pikei.api.controller.v1;

import io.pikei.api.common.dto.CameraAction;
import io.pikei.canon.CanonCameraManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Customer controller.
 */
@RestController
@RequestMapping("/v1/camera")
@Api(value = "Customer API")
public class CustomerController {

    /**
     * The Logger.
     */
    @Autowired
    private Logger logger;

    @Autowired
    private CanonCameraManager canonCameraManager;

    @Autowired
    private ServletContext servletContext;

    /**
     * Guest endpoint response entity.
     *
     * @return the response entity
     */
    @ApiOperation(value = "Test endpoint for camera actions", response = String.class)
    @PostMapping("/actions")
    public ResponseEntity<String> guestEndpoint(@RequestBody CameraAction action) throws InterruptedException {
        logger.info("Entering in camera actions endpoint, Action: {}", action.getAction());
        if( action.getAction().equals("open") ){
            canonCameraManager.open();
        } else if( action.getAction().equals("close") ){
            canonCameraManager.close();
        } else if( action.getAction().equals("connect") ){
            canonCameraManager.connect();
        } else if( action.getAction().equals("disconnect") ){
            canonCameraManager.disconnect();
        }
        return ResponseEntity.ok("Hello from customer api - GUEST");
    }

    @GetMapping(value = "/image")
    public ResponseEntity<byte[]> getImageAsResponseEntity(@RequestParam("type") String type) throws IOException, URISyntaxException {
        RandomAccessFile f = new RandomAccessFile("C:\\Users\\kosanton\\commercial-applications\\dst-project\\dst-environment\\dst-application\\output\\"+type+".jpg", "r");
        byte[] b = new byte[(int)f.length()];
        f.readFully(b);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(b, headers, HttpStatus.OK);

    }

}
