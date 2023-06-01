package io.pikei.api.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CameraAction {

    private final String action;

    @JsonCreator
    public CameraAction(@JsonProperty("action") final String action){
        this.action = action;
    }

}
