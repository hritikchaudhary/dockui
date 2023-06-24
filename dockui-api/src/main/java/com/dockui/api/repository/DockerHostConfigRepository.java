package com.dockui.api.repository;

import com.dockui.api.model.DockerHostConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Hritik Chaudhary
 */
public interface DockerHostConfigRepository extends CrudRepository<DockerHostConfig, String>, JpaRepository<DockerHostConfig, String> {
    DockerHostConfig findByWorkerId(String id);
}
