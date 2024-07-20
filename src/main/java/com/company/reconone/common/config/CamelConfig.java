package com.company.reconone.common.config;

import com.company.reconone.common.domain.Pipeline;
import com.company.reconone.common.repository.PipelineRepository;
import jakarta.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configures and initializes Camel routes for the application.
 * <p>
 * This class is responsible for creating a CamelContext, adding all routes from the PipelineScanner,
 * and starting the routes based on the startPriority and enabledPipelines configurations.
 */
@Component
public class CamelConfig {

    private static final Logger logger = LoggerFactory.getLogger(CamelConfig.class);

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private PipelineRepository pipelineStatusRepository;

    @Autowired
    private PipelineScanner pipelineScanner;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;

    // Priority for starting pipelines, either from configuration or database
    @Value("${pipelines.startPriority}")
    private String startPriority;

    // List of pipelines that should be started at application startup (when startPriority is "config")
    @Value("${pipelines.start}")
    private List<String> enabledPipelines;

    // Instance ID of the application
    @Value("${instance.id}")
    private String instanceId;

    /**
     * Initializes and starts the pipelines based on the configured start priority.
     * This method is executed after the bean's properties have been set.
     */
    @PostConstruct
    public void startPipelines() {
        new TransactionTemplate(txManager).execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    camelContext.start();
                    initializePipelines();
                } catch (Exception e) {
                    logger.error("Failed to start Camel context", e);
                    status.setRollbackOnly();
                }
            }
        });
    }

    /**
     * Initializes and starts the pipelines by scanning and instantiating route builders.
     * Routes are started based on the startPriority and enabledPipelines configurations.
     */
    private void initializePipelines() {
        // Fetch pipeline statuses from the database
        List<Pipeline> pipelineStatuses = pipelineStatusRepository.findByInstanceId(instanceId);
        Map<String, String> pipelineStatusMap = pipelineStatuses.stream()
                .collect(Collectors.toMap(Pipeline::getPipelineId, Pipeline::getStatus));

        // Clear existing pipeline statuses for this instance
        pipelineStatusRepository.deleteAllByInstanceId(instanceId);

        // Scan for route builders and add routes to the Camel context
        for (RouteBuilder routeBuilder : pipelineScanner.scanAndInstantiateRouteBuilders()) {
            try {
                camelContext.addRoutes(routeBuilder);
                String routeId = routeBuilder.getRouteCollection().getRoutes().get(0).getId();

                // Determine if the pipeline should be started based on the startPriority and enabledPipelines configurations
                if (shouldStartPipeline(routeId, pipelineStatusMap)) {
                    startPipeline(routeId);
                } else {
                    logger.info("Pipeline {} should not be started", routeId);
                }
            } catch (Exception e) {
                logger.error("Error starting pipeline {}", routeBuilder.getClass().getSimpleName(), e);
            }
        }
    }

    /**
     * Determines if a pipeline should be started based on the startPriority and enabledPipelines configurations.
     *
     * @param routeId           The route ID of the pipeline
     * @param pipelineStatusMap A map of pipeline statuses from the database
     * @return true if the pipeline should be started, false otherwise
     */
    private boolean shouldStartPipeline(String routeId, Map<String, String> pipelineStatusMap) {
        if ("config".equalsIgnoreCase(startPriority)) {
            return enabledPipelines.contains(routeId);
        } else if ("database".equalsIgnoreCase(startPriority)) {
            return "STARTED".equalsIgnoreCase(pipelineStatusMap.get(routeId));
        }
        return false;
    }

    /**
     * Starts a pipeline and updates its status in the repository.
     *
     * @param routeId The route ID of the pipeline to start
     */
    private void startPipeline(String routeId) {
        try {
            logger.info("Starting pipeline {}", routeId);
            camelContext.getRouteController().startRoute(routeId);
            pipelineStatusRepository.save(new Pipeline(routeId, instanceId, "STARTED"));
        } catch (Exception e) {
            logger.error("Error starting pipeline {}", routeId, e);
        }
    }
}
