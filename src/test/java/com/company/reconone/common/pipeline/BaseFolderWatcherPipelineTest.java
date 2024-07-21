package com.company.reconone.common.pipeline;

import com.company.reconone.common.processors.ExceptionFileLogger;
import com.company.reconone.common.processors.MdcProcessor;
import com.company.reconone.common.processors.ProcessedFileLogger;
import com.company.reconone.common.processors.StartFileLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseFolderWatcherPipelineTest {

    private BaseFolderWatcherPipeline pipeline;
    private MdcProcessor mdcProcessor;
    private StartFileLogger startFileLogger;
    private ProcessedFileLogger processedFileLogger;
    private ApplicationContext applicationContext;
    private ExceptionFileLogger exceptionFileLogger;

    @BeforeEach
    public void setUp() {
        mdcProcessor = Mockito.mock(MdcProcessor.class);
        startFileLogger = Mockito.mock(StartFileLogger.class);
        processedFileLogger = Mockito.mock(ProcessedFileLogger.class);
        applicationContext = Mockito.mock(ApplicationContext.class);
        exceptionFileLogger = Mockito.mock(ExceptionFileLogger.class);

        pipeline = new TestFolderWatcherPipeline(mdcProcessor, startFileLogger, processedFileLogger, applicationContext, exceptionFileLogger);
    }

    /**
     * Test the construction of the route URI with default processing folder and file extension.
     */
    @Test
    public void testConstructFromRoute() {
        String expectedUri = "file:sourceFolder?delay=5000&sortBy=file:modified&delete=true&preMove=inprogress&include=.*\\.txt";
        assertEquals(expectedUri, pipeline.constructFromRoute());
    }

    /**
     * Test the construction of the route URI with a custom processing folder and file extension.
     */
    @Test
    public void testConstructFromRouteWithCustomProcessingFolder() {
        pipeline = new CustomProcessingFolderPipeline(mdcProcessor, startFileLogger, processedFileLogger, applicationContext, exceptionFileLogger);
        String expectedUri = "file:sourceFolder?delay=5000&sortBy=file:modified&delete=true&preMove=customProcessing&include=.*\\.csv";
        assertEquals(expectedUri, pipeline.constructFromRoute());
    }

    /**
     * Test the construction of the route URI without specifying a file extension.
     */
    @Test
    public void testConstructFromRouteWithoutFileExtension() {
        pipeline = new NoFileExtensionPipeline(mdcProcessor, startFileLogger, processedFileLogger, applicationContext, exceptionFileLogger);
        String expectedUri = "file:sourceFolder?delay=5000&sortBy=file:modified&delete=true&preMove=inprogress";
        assertEquals(expectedUri, pipeline.constructFromRoute());
    }

    /**
     * Test the construction of the route URI with an empty processing folder.
     */
    @Test
    public void testConstructFromRouteWithEmptyProcessingFolder() {
        pipeline = new EmptyProcessingFolderPipeline(mdcProcessor, startFileLogger, processedFileLogger, applicationContext, exceptionFileLogger);
        String expectedUri = "file:sourceFolder?delay=5000&sortBy=file:modified&delete=true&include=.*\\.txt";
        assertEquals(expectedUri, pipeline.constructFromRoute());
    }

    /**
     * Test the construction of the route URI with default values (provided in the base test class).
     */
    @Test
    public void testConstructFromRouteWithDefaultValues() {
        String expectedUri = "file:sourceFolder?delay=5000&sortBy=file:modified&delete=true&preMove=inprogress&include=.*\\.txt";
        assertEquals(expectedUri, pipeline.constructFromRoute());
    }

    private static class TestFolderWatcherPipeline extends BaseFolderWatcherPipeline {
        public TestFolderWatcherPipeline(MdcProcessor mdcProcessor, StartFileLogger startFileLogger,
                                         ProcessedFileLogger processedFileLogger, ApplicationContext applicationContext,
                                         ExceptionFileLogger exceptionFileLogger) {
            super(mdcProcessor, startFileLogger, processedFileLogger, applicationContext, exceptionFileLogger);
        }

        @Override
        public String getPipelineName() {
            return "testPipeline";
        }

        @Override
        public Object getProcessor() {
            return new Object();
        }

        @Override
        public String sourceFolder() {
            return "sourceFolder";
        }

        @Override
        public String fileExtension() {
            return "txt";
        }
    }

    private static class CustomProcessingFolderPipeline extends BaseFolderWatcherPipeline {
        public CustomProcessingFolderPipeline(MdcProcessor mdcProcessor, StartFileLogger startFileLogger,
                                              ProcessedFileLogger processedFileLogger, ApplicationContext applicationContext,
                                              ExceptionFileLogger exceptionFileLogger) {
            super(mdcProcessor, startFileLogger, processedFileLogger, applicationContext, exceptionFileLogger);
        }

        @Override
        public String getPipelineName() {
            return "customPipeline";
        }

        @Override
        public Object getProcessor() {
            return new Object();
        }

        @Override
        public String sourceFolder() {
            return "sourceFolder";
        }

        @Override
        public String processingFolder() {
            return "customProcessing";
        }

        @Override
        public String fileExtension() {
            return "csv";
        }
    }

    private static class NoFileExtensionPipeline extends BaseFolderWatcherPipeline {
        public NoFileExtensionPipeline(MdcProcessor mdcProcessor, StartFileLogger startFileLogger,
                                       ProcessedFileLogger processedFileLogger, ApplicationContext applicationContext,
                                       ExceptionFileLogger exceptionFileLogger) {
            super(mdcProcessor, startFileLogger, processedFileLogger, applicationContext, exceptionFileLogger);
        }

        @Override
        public String getPipelineName() {
            return "noExtensionPipeline";
        }

        @Override
        public Object getProcessor() {
            return new Object();
        }

        @Override
        public String sourceFolder() {
            return "sourceFolder";
        }

        @Override
        public String fileExtension() {
            return null;
        }
    }

    private static class EmptyProcessingFolderPipeline extends BaseFolderWatcherPipeline {
        public EmptyProcessingFolderPipeline(MdcProcessor mdcProcessor, StartFileLogger startFileLogger,
                                             ProcessedFileLogger processedFileLogger, ApplicationContext applicationContext,
                                             ExceptionFileLogger exceptionFileLogger) {
            super(mdcProcessor, startFileLogger, processedFileLogger, applicationContext, exceptionFileLogger);
        }

        @Override
        public String getPipelineName() {
            return "emptyProcessingFolderPipeline";
        }

        @Override
        public Object getProcessor() {
            return new Object();
        }

        @Override
        public String sourceFolder() {
            return "sourceFolder";
        }

        @Override
        public String processingFolder() {
            return "";
        }

        @Override
        public String fileExtension() {
            return "txt";
        }
    }
}
