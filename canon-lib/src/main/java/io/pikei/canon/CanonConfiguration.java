package io.pikei.canon;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import io.pikei.canon.framework.api.helper.initialisation.FrameworkInitialisation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
/**
 * The type Security configuration.
 */
@Configuration
@Slf4j
@Getter
public class CanonConfiguration{

    @Value("${canon.camera.serial}")
    private String serialNumber;

    @PostConstruct
    public void init(){
        log.info("Initializing Canon SDK Framework...");
        new FrameworkInitialisation()
                .registerCameraAddedEvent()
                .withEventFetcherLogic()
                .initialize();
    }

}
