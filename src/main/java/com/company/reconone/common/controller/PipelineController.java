package com.company.reconone.common.controller;

import com.company.reconone.common.controller.model.PipelineStatusResponse;
import com.company.reconone.common.domain.Pipeline;
import com.company.reconone.common.domain.PipelineId;
import com.company.reconone.common.domain.ProcessingInfo;
import com.company.reconone.common.repository.FileProcessingRepository;
import com.company.reconone.common.repository.MessageProcessingRepository;
import com.company.reconone.common.repository.PipelineRepository;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.apache.camel.spi.RouteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for managing the pipelines.
 */
@Controller
@RequestMapping("/pipelines")
public class PipelineController {

    Logger log = LoggerFactory.getLogger(PipelineController.class);

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private PipelineRepository pipelineStatusRepository;

    @Autowired
    private FileProcessingRepository fileProcessingRepository;

    @Autowired
    private MessageProcessingRepository messageProcessingRepository;

    @Value("${instance.id}")
    private String instanceId;

    /**
     * This method fetches all the pipelines from the CamelContext and displays them in a Thymeleaf template.
     *
     * @return Template name for the pipeline list view
     */
    @GetMapping
    public String getAllPipelines(Model model) {
        List<PipelineStatusResponse> pipelines = camelContext.getRoutes().stream()
                .map(route -> {
                    String routeId = route.getId();
                    String status = getPipelineStatus(routeId);
                    return new PipelineStatusResponse(routeId, status);
                })
                .collect(Collectors.toList());
        model.addAttribute("pipelines", pipelines);
        return "pipelineList";
    }

    @GetMapping("/{id}/status")
    @ResponseBody
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
    public String startPipeline(@PathVariable String id, Model model) {
        String message;
        try {
            camelContext.getRouteController().startRoute(id);
            updatePipelineStatus(id, "STARTED");
            message = "Pipeline " + id + " started successfully.";
        } catch (Exception e) {
            message = "Error starting pipeline " + id + ": " + e.getMessage();
            log.error(message, e);
        }
        model.addAttribute("message", message);
        return "redirect:/pipelines";
    }

    /**
     * This method stops a pipeline.
     *
     * @param id Pipeline ID
     * @return Pipeline stop status
     */
    @PostMapping("/stop/{id}")
    public String stopPipeline(@PathVariable String id, Model model) {
        String message;
        try {
            camelContext.getRouteController().stopRoute(id);
            updatePipelineStatus(id, "STOPPED");
            message = "Pipeline " + id + " stopped successfully.";
        } catch (Exception e) {
            message = "Error stopping pipeline " + id + ": " + e.getMessage();
            log.error(message, e);
        }
        model.addAttribute("message", message);
        return "redirect:/pipelines";
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

    /**
     * This method fetches the processing statistics for a specific pipeline.
     *
     * @param id Pipeline ID
     * @return Template name for the statistics view
     */
    @GetMapping("/statistics/{pipelineId}")
    public String getPipelineStatistics(@PathVariable String pipelineId, Model model) {
        List<ProcessingInfo> processingInfos = new ArrayList<>();
        processingInfos.addAll(fileProcessingRepository.findAllByPipelineId(pipelineId));
        processingInfos.addAll(messageProcessingRepository.findAllByPipelineId(pipelineId));
        model.addAttribute("processingInfos", processingInfos);

        return "pipelineStatistics";
    }
}
