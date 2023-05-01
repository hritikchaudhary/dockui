package ai.openfabric.api.repository;

import ai.openfabric.api.model.DockerNetworkSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DockerNetworkSettingsRepository extends CrudRepository<DockerNetworkSettings, String>, JpaRepository<DockerNetworkSettings, String> {
}
