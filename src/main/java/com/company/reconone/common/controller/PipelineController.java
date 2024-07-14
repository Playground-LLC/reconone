package com.company.reconone.common.controller;

import com.company.reconone.common.controller.model.PipelineStatusResponse;
import com.company.reconone.common.domain.Pipeline;
import com.company.reconone.common.domain.PipelineId;
import com.company.reconone.common.repository.PipelineRepository;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.apache.camel.spi.RouteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for managing the pipelines.
 */
@RestController
@RequestMapping("/api/pipelines")
public class PipelineController {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private PipelineRepository pipelineStatusRepository;

    @Value("${instance.id}")
    private String instanceId;

    /**
     * This method fetches all the pipelines from the CamelContext.
     *
     * @return List of pipeline IDs
     */
    @GetMapping
    public List<PipelineStatusResponse> getAllPipelines() {
        return camelContext.getRoutes().stream()
                .map(route -> {
                    String routeId = route.getId();
                    String status = getPipelineStatus(routeId);
                    return new PipelineStatusResponse(routeId, status);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/status")
    public String getPipelineStatus(@PathVariable String id) {
        Route route = camelContext.getRoute(id);
        if (route != null) {
            RouteController routeController = camelContext.getRouteController();
            ServiceStatus status = routeController.getRouteStatus(id);
            return status != null ? status.name() : "UNKNOWN";
        } else {
            return "Pipeline " + id + " not found.";
        }
    }

    /**
     * This method fetches the status of a pipeline.
     *
     * @param id Pipeline ID
     * @return Pipeline status
     */
    @PostMapping("/start/{id}")
    public String startPipeline(@PathVariable String id) {
        try {
            camelContext.getRouteController().startRoute(id);
            updatePipelineStatus(id, "STARTED");
            return "Pipeline " + id + " started successfully.";
        } catch (Exception e) {
            return "Error starting pipeline " + id + ": " + e.getMessage();
        }
    }

    /**
     * This method stops a pipeline.
     *
     * @param id Pipeline ID
     * @return Pipeline stop status
     */
    @PostMapping("/stop/{id}")
    public String stopPipeline(@PathVariable String id) {
        try {
            camelContext.getRouteController().stopRoute(id);
            updatePipelineStatus(id, "STOPPED");
            return "Pipeline " + id + " stopped successfully.";
        } catch (Exception e) {
            return "Error stopping pipeline " + id + ": " + e.getMessage();
        }
    }

    /**
     * This method updates the status of a pipeline in the database.
     *
     * @param pipelineId Pipeline ID
     * @param status     Pipeline status
     */
    private void updatePipelineStatus(String pipelineId, String status) {
        Pipeline pipelineStatus = pipelineStatusRepository.findById(new PipelineId(pipelineId, instanceId))
                .orElse(new Pipeline());
        pipelineStatus.setPipelineId(pipelineId);
        pipelineStatus.setInstanceId(instanceId);
        pipelineStatus.setStatus(status);
        pipelineStatusRepository.save(pipelineStatus);
    }
}
