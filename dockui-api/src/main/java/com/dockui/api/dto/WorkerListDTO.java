package com.dockui.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Hritik Chaudhary
 */
public class WorkerListDTO {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String containerId;

    public WorkerListDTO(String name, String containerId) {
        this.name = name;
        this.containerId = containerId;
    }
}
