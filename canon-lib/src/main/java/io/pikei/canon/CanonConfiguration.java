package io.pikei.canon;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The security configuration.
 */
@Configuration
@Slf4j
@Getter
public class CanonConfiguration{

    @Value("${canon.camera.serial}")
    private String serialNumber;

}
