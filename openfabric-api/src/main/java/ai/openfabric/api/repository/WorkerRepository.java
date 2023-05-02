package ai.openfabric.api.repository;

import ai.openfabric.api.dto.WorkerListDTO;
import ai.openfabric.api.model.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WorkerRepository extends CrudRepository<Worker, String>, JpaRepository<Worker, String> {

    Worker findByImageId(String id);

    @Query("SELECT new ai.openfabric.api.dto.WorkerListDTO(w.name, w.containerId) FROM Worker w")
    Page<WorkerListDTO> listAllWorkers(Pageable pageable);

    Worker findByContainerId(String containerId);
}
