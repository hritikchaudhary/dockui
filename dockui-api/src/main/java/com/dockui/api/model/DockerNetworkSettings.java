package com.dockui.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hritik Chaudhary
 */
@Entity
public class DockerNetworkSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @OneToMany(mappedBy = "dockerNetworkSettings", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<DockerNetwork> networks;

    public void setNetworks(List<DockerNetwork> newNetworks) {
        if (this.networks == null) {
            this.networks = new HashSet<>();
        }
        for (DockerNetwork network : newNetworks) {
            if (!this.networks.contains(network)) {
                network.setDockerNetworkSettings(this);
                this.networks.add(network);
            }
        }
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    @Getter
    @Setter
    @JsonIgnore
    private Worker worker;

}
