package com.company.reconone.common.config;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for scanning the application context for RouteBuilders and instantiating them.
 */
@Component
public class PipelineScanner {

    Logger logger = LoggerFactory.getLogger(PipelineScanner.class);

    private final ApplicationContext applicationContext;

    // List of pipelines that should be enabled, so they can be started and stopped
    @Value("${pipelines.enabled}")
    private List<String> enabledPipelines;

    public PipelineScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * This method scans the application context for implementations of RouteBuilders and instantiates them.
     *
     * It will only instantiate RouteBuilders that are enabled in the application.properties file.
     * This is to make sure, one instance of VM cannot start all the pipelines, but only the ones that are enabled for that instance.
     *
     * @return List of RouteBuilders
     */
    public List<RouteBuilder> scanAndInstantiateRouteBuilders() {
        List<RouteBuilder> routeBuilders = new ArrayList<>();

        // Scan the application context for RouteBuilders
        for (String beanName : applicationContext.getBeanNamesForType(RouteBuilder.class)) {
            if(enabledPipelines.contains(beanName)) {
                logger.info("Instantiating pipeline {}", beanName);
                RouteBuilder routeBuilder = (RouteBuilder) applicationContext.getBean(beanName);
                routeBuilders.add(routeBuilder);
            } else {
                logger.info("Pipeline {} is not enabled, skipping instantiation", beanName);
            }
        }

        return routeBuilders;
    }
}

