package com.dockui.api.repository;

import com.dockui.api.model.DockerNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Hritik Chaudhary
 */
public interface DockerNetworkRepository extends CrudRepository<DockerNetwork, String>, JpaRepository<DockerNetwork, String> {
}
