package ai.openfabric.api.service;

import ai.openfabric.api.model.*;
import ai.openfabric.api.repository.*;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerMount;
import com.github.dockerjava.api.model.ContainerPort;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
            worker.setName(container.getNames()[0].substring(1)); // remove the leading '/' from the container name
            worker.setCommand(container.getCommand());
            worker.setCreated(container.getCreated());
            worker.setImage(container.getImage());
            worker.setImageId(container.getImageId());
            List<DockerPort> dockerPorts = this.dockerPortRepository.findByWorkerId(worker.getId());
            for (ContainerPort exposedPort : container.getPorts()) {
                DockerPort dockerPort = new DockerPort();
                dockerPort.setPrivatePort(exposedPort.getPrivatePort());
                dockerPort.setWorker(worker);
                dockerPorts.add(dockerPort);
            }
            worker.setPorts(dockerPorts);
            worker.setStatus(container.getStatus());
            worker.setState(container.getState());
            worker.setSizeRw(container.getSizeRw());
            worker.setSizeRootFs(container.getSizeRootFs());

            DockerHostConfig hostConfig = dockerHostConfigRepository.findByWorkerId(worker.getId());
            if (hostConfig == null) {
                hostConfig = new DockerHostConfig();
                hostConfig.setWorker(worker);
            }
            worker.setHostConfig(hostConfig);
            DockerNetworkSettings dockerNetworkSettings = dockerNetworkSettingsRepository.findByWorkerId(worker.getId());
            if(dockerNetworkSettings==null){
                dockerNetworkSettings = new DockerNetworkSettings();
                dockerNetworkSettings.setWorker((worker));
            }
            worker.setNetworkSettings(dockerNetworkSettings);

            List<DockerMount> dockerMounts = dockerMountRepository.findByWorkerId(worker.getId());
            worker.setMounts(dockerMounts);
            for (ContainerMount containerMount : container.getMounts()) {
                DockerMount dockerMount = new DockerMount();
                dockerMount.setWorker(worker);
                dockerMount.setName(containerMount.getName());
                dockerMount.setRw(containerMount.getRw());
                dockerMounts.add(dockerMount);
            }
            worker.setMounts(dockerMounts);
            workerRepository.save(worker);
        }
        return "Synced successfully!";
    }

    public List<Container> getContainers(){
        return dockerClient.listContainersCmd().withShowAll(true).exec();
    }
    public List<Worker> getWorkersFromDb(){
        return workerRepository.findAll();
    }
}
