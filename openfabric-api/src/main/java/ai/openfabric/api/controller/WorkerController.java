package ai.openfabric.api.controller;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.service.WorkerService;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Container;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {

    private WorkerService workerService;
    public WorkerController(WorkerService workerService) {
        // initialize DockerClient and other resources
        this.workerService = workerService;
        this.workerService.syncWorkersFromDocker();
    }


    @PostMapping(path = "/hello")
    public @ResponseBody String hello(@RequestBody String name) {
        return "Hello!" + name;
    }

    @GetMapping(path = "/containers")
    public List<Container> getContainers() throws DockerException, InterruptedException {
        return this.workerService.getContainers();
    }

    @GetMapping(path = "/workers")
    public List<Worker> getWorkersFromDb() {
        return this.workerService.getWorkersFromDb();
    }

    @GetMapping(path = "/sync")
    public String syncWorkersFromDocker() {
        return this.workerService.syncWorkersFromDocker();
    }
}
