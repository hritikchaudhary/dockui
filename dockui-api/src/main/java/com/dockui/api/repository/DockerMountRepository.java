package com.dockui.api.repository;

import com.dockui.api.model.DockerMount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 *
 * @author Hritik Chaudhary
 */
public interface DockerMountRepository extends CrudRepository<DockerMount, String>, JpaRepository<DockerMount, String> {

    List<DockerMount> findByWorkerId(String id);
}
