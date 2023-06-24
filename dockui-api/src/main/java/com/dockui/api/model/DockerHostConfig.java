package com.dockui.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

/**
 * @author Hritik Chaudhary
 */
@Entity
public class DockerHostConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String networkMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    @Getter
    @Setter
    @JsonIgnore
    private Worker worker;
}
