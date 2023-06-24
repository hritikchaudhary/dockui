package com.dockui.api.repository;

import com.dockui.api.dto.WorkerListDTO;
import com.dockui.api.model.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WorkerRepository extends CrudRepository<Worker, String>, JpaRepository<Worker, String> {

    Worker findByImageId(String id);

    @Query("SELECT new com.dockui.api.dto.WorkerListDTO(w.name, w.containerId) FROM Worker w")
    Page<WorkerListDTO> listAllWorkers(Pageable pageable);

    Worker findByContainerId(String containerId);
}
