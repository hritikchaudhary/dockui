package ai.openfabric.api.repository;

import ai.openfabric.api.model.DockerPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DockerPortRepository extends CrudRepository<DockerPort, String>, JpaRepository<DockerPort, String> {
    List<DockerPort> findByWorkerId(String id);
}
