package ai.openfabric.api.repository;

import ai.openfabric.api.model.DockerMount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DockerMountRepository extends CrudRepository<DockerMount, String>, JpaRepository<DockerMount, String> {

}
