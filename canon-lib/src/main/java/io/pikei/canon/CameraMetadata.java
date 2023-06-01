package io.pikei.canon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CameraMetadata {
    private final String productName;
    private final String imageQuality;
    private final String firmwareVersion;
    private final String serialNumber;
    private final String batteryLevel;
}
