package ai.openfabric.api.repository;

import ai.openfabric.api.model.DockerNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DockerNetworkRepository extends CrudRepository<DockerNetwork, String>, JpaRepository<DockerNetwork, String> {
}
