package com.dockui.api.controller;

import com.dockui.api.dto.WorkerListDTO;
import com.dockui.api.model.Worker;
import com.dockui.api.service.WorkerService;
import com.github.dockerjava.api.model.Statistics;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping(path = "/list")
    public ResponseEntity<Page<WorkerListDTO>> getWorkersFromDb(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Page<WorkerListDTO> workerPage = workerService.getWorkersFromDb(page, size);
        return new ResponseEntity<>(workerPage, HttpStatus.OK);
    }

    @GetMapping(path = "/syncWithDocker")
    public String syncWorkersFromDocker() {
        return this.workerService.syncWorkersFromDocker();
    }

    @PostMapping("/start/{containerId}")
    public ResponseEntity<String> startContainer(@PathVariable String containerId) {
        return this.workerService.startContainer(containerId);
    }

    @PostMapping("/stop/{containerId}")
    public ResponseEntity<String> stopContainer(@PathVariable String containerId) {
        return this.workerService.stopContainer(containerId);
    }

    @GetMapping(path = "/info/{containerId}")
    public Worker getWorkerInformation(@PathVariable String containerId) {
        return this.workerService.getWorkerInformation(containerId);
    }

    @GetMapping(path = "/stats/{containerId}")
    public ResponseEntity<Object> getContainerStatistics(@PathVariable String containerId) {
        try {
            Statistics statistics = this.workerService.getContainerStatistics(containerId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            String errorMessage = "Failed to get container statistics: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

}
