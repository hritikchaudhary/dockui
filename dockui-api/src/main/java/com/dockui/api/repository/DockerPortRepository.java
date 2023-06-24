package com.dockui.api.repository;

import com.dockui.api.model.DockerPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Hritik Chaudhary
 */
public interface DockerPortRepository extends CrudRepository<DockerPort, String>, JpaRepository<DockerPort, String> {
    List<DockerPort> findByWorkerId(String id);
}
