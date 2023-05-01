package ai.openfabric.api.controller;

import ai.openfabric.api.model.*;
import ai.openfabric.api.repository.DockerHostConfigRepository;
import ai.openfabric.api.repository.WorkerRepository;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerMount;
import com.github.dockerjava.api.model.ContainerPort;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {

    private WorkerRepository workerRepository;
    private DockerHostConfigRepository dockerHostConfigRepository;
    private DockerClient dockerClient;

    public WorkerController(WorkerRepository workerRepository,DockerHostConfigRepository dockerHostConfigRepository) {
        // initialize DockerClient and other resources
        this.workerRepository = workerRepository;
        this.dockerHostConfigRepository = dockerHostConfigRepository;
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


    @PostMapping(path = "/hello")
    public @ResponseBody String hello(@RequestBody String name) {
        return "Hello!" + name;
    }

    @GetMapping(path = "/containers")
    public List<Container> getContainers() throws DockerException, InterruptedException {
        return dockerClient.listContainersCmd().withShowAll(true).exec();
    }

    @GetMapping(path = "/workers")
    public List<Worker> getWorkersFromDb() {
        return workerRepository.findAll();
    }

    @GetMapping(path = "/sync")
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
            worker.setPorts(new ArrayList<>());
            for (ContainerPort exposedPort : container.getPorts()) {
                DockerPort dockerPort = new DockerPort();
                dockerPort.setPrivatePort(exposedPort.getPrivatePort());
                dockerPort.setWorker(worker);
                worker.getPorts().add(dockerPort);
            }
            worker.setStatus(container.getStatus());
            worker.setState(container.getState());
            worker.setSizeRw(container.getSizeRw());
            worker.setSizeRootFs(container.getSizeRootFs());

            DockerHostConfig hostConfig = dockerHostConfigRepository.findByWorkerId(worker.getId());
            if(hostConfig==null){
                hostConfig = new DockerHostConfig();
                hostConfig.setWorker(worker);
            }
            worker.setHostConfig(hostConfig);
            DockerNetworkSettings dockerNetworkSettings = new DockerNetworkSettings();
            dockerNetworkSettings.setWorker(worker);
            worker.setNetworkSettings(dockerNetworkSettings);

            worker.setMounts(new ArrayList<>());
            for (ContainerMount containerMount : container.getMounts()) {
                DockerMount dockerMount = new DockerMount();
                dockerMount.setWorker(worker);
                dockerMount.setName(containerMount.getName());
                dockerMount.setRw(containerMount.getRw());
                worker.getMounts().add(dockerMount);
            }
            workerRepository.save(worker);
        }
        return "Synced successfully!";
    }
}
