package ai.openfabric.api.service;

import ai.openfabric.api.dto.WorkerListDTO;
import ai.openfabric.api.model.*;
import ai.openfabric.api.repository.*;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final DockerHostConfigRepository dockerHostConfigRepository;
    private final DockerNetworkSettingsRepository dockerNetworkSettingsRepository;

    private final DockerMountRepository dockerMountRepository;

    private final DockerPortRepository dockerPortRepository;
    private final DockerClient dockerClient;

    public WorkerService(WorkerRepository workerRepository,
                         DockerHostConfigRepository dockerHostConfigRepository,
                         DockerNetworkSettingsRepository dockerNetworkSettingsRepository,
                         DockerPortRepository dockerPortRepository,
                         DockerMountRepository dockerMountRepository) {
        // initialize DockerClient and other resources
        this.workerRepository = workerRepository;
        this.dockerHostConfigRepository = dockerHostConfigRepository;
        this.dockerNetworkSettingsRepository = dockerNetworkSettingsRepository;
        this.dockerPortRepository = dockerPortRepository;
        this.dockerMountRepository = dockerMountRepository;
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        dockerClient = DockerClientImpl.getInstance(config, httpClient);
    }

    public String syncWorkersFromDocker() {
        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
        for (Container container : containers) {
            Worker worker = workerRepository.findByImageId(container.getImageId());
            if (worker == null) {
                worker = new Worker();
                worker.setImageId(container.getImageId());
            }
            worker.setContainerId(container.getId());
            worker.setName(container.getNames()[0].substring(1)); // remove the leading '/' from the container name
            worker.setCommand(container.getCommand());
            worker.setImage(container.getImage());
            worker.setImageId(container.getImageId());
            worker.setStatus(container.getStatus());
            worker.setState(container.getState());
            worker.setSizeRw(container.getSizeRw());
            worker.setSizeRootFs(container.getSizeRootFs());
            InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
            Timestamp createdDate = Timestamp.from(Instant.parse(containerInfo.getCreated()));
            Timestamp updatedDate = Timestamp.from(Instant.parse(containerInfo.getState().getStartedAt()));
            Timestamp deletedDate = Timestamp.from(Instant.parse(containerInfo.getState().getFinishedAt()));
            worker.setCreatedAt(createdDate);
            worker.setUpdatedAt(updatedDate);
            worker.setDeletedAt(deletedDate);

            List<DockerPort> dockerPorts = this.dockerPortRepository.findByWorkerId(worker.getId());
            for (ContainerPort containerPort : container.getPorts()) {
                DockerPort dockerPort = new DockerPort();
                dockerPort.setIp(containerPort.getIp());
                dockerPort.setPrivatePort(containerPort.getPrivatePort());
                dockerPort.setPublicPort(containerPort.getPublicPort());
                dockerPort.setType(containerPort.getType());
                dockerPort.setWorker(worker);
                dockerPorts.add(dockerPort);
            }
            worker.setPorts(dockerPorts);
            DockerHostConfig hostConfig = dockerHostConfigRepository.findByWorkerId(worker.getId());
            if (hostConfig == null) {
                hostConfig = new DockerHostConfig();
                hostConfig.setWorker(worker);
            }
            hostConfig.setNetworkMode(container.getHostConfig().getNetworkMode());
            worker.setHostConfig(hostConfig);
            DockerNetworkSettings dockerNetworkSettings = dockerNetworkSettingsRepository.findByWorkerId(worker.getId());
            if (dockerNetworkSettings == null) {
                dockerNetworkSettings = new DockerNetworkSettings();
                dockerNetworkSettings.setWorker((worker));
            }
            List<DockerNetwork> dockerNetworks = new ArrayList<>();
            for (ContainerNetwork containerNetwork : container.getNetworkSettings().getNetworks().values()) {
                DockerNetwork dockerNetwork = new DockerNetwork();
                dockerNetwork.setDockerNetworkSettings(dockerNetworkSettings);
                dockerNetwork.setAliases(containerNetwork.getAliases());
                dockerNetwork.setGateway(containerNetwork.getGateway());
                dockerNetwork.setEndpointId(containerNetwork.getEndpointId());
                dockerNetwork.setIpAddress(containerNetwork.getIpAddress());
                dockerNetwork.setGlobalIpv6Address(containerNetwork.getGlobalIPv6Address());
                dockerNetwork.setGlobalIpv6PrefixLen(containerNetwork.getGlobalIPv6PrefixLen());
                dockerNetwork.setIpPrefixLen(containerNetwork.getIpPrefixLen());
                dockerNetwork.setIpv6Gateway(containerNetwork.getIpV6Gateway());
                dockerNetwork.setMacAddress(containerNetwork.getMacAddress());
                dockerNetworks.add(dockerNetwork);
            }
            dockerNetworkSettings.setNetworks(dockerNetworks);
            worker.setNetworkSettings(dockerNetworkSettings);
            List<DockerMount> dockerMounts = dockerMountRepository.findByWorkerId(worker.getId());
            worker.setMounts(dockerMounts);
            for (ContainerMount containerMount : container.getMounts()) {
                DockerMount dockerMount = new DockerMount();
                dockerMount.setWorker(worker);
                dockerMount.setRw(containerMount.getRw());
                dockerMount.setName(containerMount.getName());
                dockerMount.setMode(containerMount.getMode());
                dockerMount.setDriver(containerMount.getDriver());
                dockerMount.setDestination(containerMount.getDestination());
                dockerMount.setSource(containerMount.getSource());
                dockerMount.setMode(containerMount.getMode());
                dockerMount.setPropagation(containerMount.getPropagation());
                dockerMounts.add(dockerMount);
            }
            worker.setMounts(dockerMounts);
            workerRepository.save(worker);
        }
        return "Synced successfully!";
    }

    public Page<WorkerListDTO> getWorkersFromDb(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return workerRepository.listAllWorkers(pageable);
    }

    public ResponseEntity<String> startContainer(String containerId) {
        try {
            if (dockerClient.inspectContainerCmd(containerId).exec().getState().getStatus().equals("running")) {
                return ResponseEntity.ok("Container is already running!");
            }
            dockerClient.startContainerCmd(containerId).exec();
            return ResponseEntity.ok("Container started successfully!");
        } catch (DockerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start container: " + e.getMessage());
        }

    }

    public ResponseEntity<String> stopContainer(String containerId) {
        try {
            if (dockerClient.inspectContainerCmd(containerId).exec().getState().getStatus().equals("exited")) {
                return ResponseEntity.ok("Container is already stopped!");
            }
            dockerClient.stopContainerCmd(containerId).exec();
            return ResponseEntity.ok("Container stopped successfully!");
        } catch (DockerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to stop container: " + e.getMessage());
        }
    }

    public Worker getWorkerInformation(String containerId){
        return workerRepository.findByContainerId(containerId);
    }

    public Statistics getContainerStatistics(String containerId) throws Exception {
        InvocationBuilder.AsyncResultCallback<Statistics> callback = new InvocationBuilder.AsyncResultCallback<>();
        dockerClient.statsCmd(containerId).exec(callback);
        Statistics stats;
        try {
            stats = callback.awaitResult();
            callback.close();
            return stats;
        } catch (Exception e) {
            throw new Exception("Failed to get container statistics: " + e.getMessage());
        }
    }
}
