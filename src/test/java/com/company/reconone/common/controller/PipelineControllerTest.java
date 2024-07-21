package com.company.reconone.common.controller;

import com.company.reconone.common.domain.FileProcessingInfo;
import com.company.reconone.common.domain.MessageProcessingInfo;
import com.company.reconone.common.domain.Pipeline;
import com.company.reconone.common.service.PipelineService;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.apache.camel.spi.RouteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PipelineControllerTest {

    private PipelineController pipelineController;
    private CamelContext camelContext;
    private PipelineService pipelineService;
    private RouteController routeController;
    private Model model;

    @BeforeEach
    public void setUp() {
        camelContext = Mockito.mock(CamelContext.class);
        pipelineService = Mockito.mock(PipelineService.class);
        routeController = Mockito.mock(RouteController.class);
        model = Mockito.mock(Model.class);

        when(camelContext.getRouteController()).thenReturn(routeController);

        pipelineController = new PipelineController(camelContext, pipelineService);
    }

    /**
     * Test fetching all pipelines and displaying them in a Thymeleaf template.
     */
    @Test
    public void testGetAllPipelines() {
        Route route = Mockito.mock(Route.class);
        when(route.getId()).thenReturn("testRoute");
        when(camelContext.getRoutes()).thenReturn(List.of(route));
        when(routeController.getRouteStatus("testRoute")).thenReturn(ServiceStatus.Started);

        String viewName = pipelineController.getAllPipelines(model);
        assertEquals("pipelineList", viewName);

        verify(model).addAttribute(eq("pipelines"), any());
    }

    /**
     * Test fetching the status of a pipeline.
     */
    @Test
    public void testGetPipelineStatus() {
        Route route = Mockito.mock(Route.class);
        when(camelContext.getRoute("testRoute")).thenReturn(route);
        when(routeController.getRouteStatus("testRoute")).thenReturn(ServiceStatus.Started);

        String status = pipelineController.getPipelineStatus("testRoute");
        assertEquals("Started", status);
    }

    /**
     * Test starting a pipeline.
     */
    @Test
    public void testStartPipeline() throws Exception {
        doNothing().when(routeController).startRoute("testRoute");

        String viewName = pipelineController.startPipeline("testRoute", model);
        assertEquals("redirect:/pipelines", viewName);

        verify(model).addAttribute(eq("message"), eq("Pipeline testRoute started successfully."));
        verify(pipelineService).savePipeline(any(Pipeline.class));
    }

    /**
     * Test stopping a pipeline.
     */
    @Test
    public void testStopPipeline() throws Exception {
        doNothing().when(routeController).stopRoute("testRoute");

        String viewName = pipelineController.stopPipeline("testRoute", model);
        assertEquals("redirect:/pipelines", viewName);

        verify(model).addAttribute(eq("message"), eq("Pipeline testRoute stopped successfully."));
        verify(pipelineService).savePipeline(any(Pipeline.class));
    }

    /**
     * Test fetching the processing statistics for a specific pipeline.
     */
    @Test
    public void testGetPipelineStatistics() {
        List<FileProcessingInfo> fileProcessingInfos = new ArrayList<>();
        List<MessageProcessingInfo> messageProcessingInfos = new ArrayList<>();
        when(pipelineService.findFileProcessingByPipelineId("testPipeline")).thenReturn(fileProcessingInfos);
        when(pipelineService.findMessageProcessingByPipelineId("testPipeline")).thenReturn(messageProcessingInfos);

        String viewName = pipelineController.getPipelineStatistics("testPipeline", model);
        assertEquals("pipelineStatistics", viewName);

        verify(model).addAttribute(eq("processingInfos"), any());
    }
}

