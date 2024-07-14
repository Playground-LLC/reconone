package com.company.reconone.common.config;

import com.company.reconone.common.domain.Pipeline;
import com.company.reconone.common.repository.PipelineRepository;
import jakarta.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.boot.SpringBootCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is responsible for creating a CamelContext and adding all the routes from the pipelineScanner.
 * It also starts the routes based on the startPriority and enabledPipelines.
 */
@Component
public class CamelConfig {

    Logger logger = LoggerFactory.getLogger(CamelConfig.class);

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private PipelineRepository pipelineStatusRepository;

    @Autowired
    private PipelineScanner pipelineScanner;

    // Value can be (config or database), decides the priority of starting the pipelines
    @Value("${pipelines.startPriority}")
    private String startPriority;

    // List of pipelines that should be started at the application startup
    @Value("${pipelines.start}")
    private List<String> enabledPipelines;

    // Instance ID of the application
    @Value("${instance.id}")
    private String instanceId;


    /**
     * This method starts the routes based on the startPriority and enabledPipelines.
     *
     */
    @PostConstruct
    public void startPipelines() throws Exception {
        camelContext.start();

        // Fetch pipeline statuses from the database
        List<Pipeline> pipelineStatuses = pipelineStatusRepository.findByInstanceId(instanceId);
        Map<String, String> pipelineStatusMap = pipelineStatuses.stream()
                .collect(Collectors.toMap(Pipeline::getPipelineId, Pipeline::getStatus));

        for (RouteBuilder routeBuilder : pipelineScanner.scanAndInstantiateRouteBuilders()) {
            camelContext.addRoutes(routeBuilder);

            String routeId = routeBuilder.getRouteCollection().getRoutes().get(0).getId();
            // Start the route if it should be started
            boolean shouldStart = shouldStartPipeline(routeId, pipelineStatusMap);
            if (shouldStart) {
                logger.info("Starting pipeline {}", routeId);
                camelContext.getRouteController().startRoute(routeId);
            } else {
                logger.info("Pipeline {} should not be started", routeId);
            }
        }
    }

    /**
     * This method checks if a pipeline should be started based on the startPriority and enabledPipelines.
     *
     * @param routeId routeId
     * @param pipelineStatusMap pipelineStatusMap with pipelineId as key and status as value
     * @return boolean indicating if the pipeline should be started
     */
    private boolean shouldStartPipeline(String routeId, Map<String, String> pipelineStatusMap) {
        if ("config".equalsIgnoreCase(startPriority)) {
            return enabledPipelines.contains(routeId);
        } else if ("database".equalsIgnoreCase(startPriority)) {
            return "STARTED".equalsIgnoreCase(pipelineStatusMap.get(routeId));
        }
        return false;
    }

}

