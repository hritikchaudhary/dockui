package com.dockui.api.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;


@MappedSuperclass
public class Datable implements Serializable {

    @Schema(hidden = true)
    public Date createdAt;

    @Schema(hidden = true)
    public Date updatedAt;

    @Schema(hidden = true)
    public Date deletedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = new Date();
    }

}
