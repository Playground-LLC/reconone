# ETL Pipeline for Recon Jobs

This project provides an Apache Camel-based ETL pipeline framework designed for reconciliation (recon) jobs. It supports both batch and stream processing with built-in observability features, allowing pipelines to be managed via a REST API and configured for auto start/stop based on configuration or database entries.

## Key Features

- **Apache Camel Integration**: Utilizes Apache Camel for robust and flexible routing and processing.
- **Batch and Stream Processing**: Supports different types of pipelines for various processing needs.
- **Observability**: Includes built-in observability features for monitoring and logging.
- **REST API**: Pipelines can be started and stopped using a REST API.
- **Auto Start/Stop**: Pipelines can automatically start and stop based on configuration or database settings.

## Getting Started

Start the application by running the command:
```shell
java -jar etl.jar --spring.config.additional-location=file:src/main/resources/pipelineconfigs/*/
```

## Pipeline Creation

### Sample Pipelines

- **Folder Watcher**: Batch processing example.
    - Package: `com.company.etl.pipeline.reconone.pipelines.pipeline1`
- **Kafka Consumer**: Stream processing example.
    - Package: `com.company.etl.pipeline.reconone.pipelines.pipeline2`
- **Large File Processing**: Batch processing for large files.
    - Package: `com.company.etl.pipeline.reconone.pipelines.pipeline3`

### Pipeline Configuration

You can find the pipeline configuration in path: src/main/resources/pipelineconfigs/`pipelinename`/application.yml

## Folder Watcher Pipeline

To create a new pipeline, extend the `BaseFolderWatcherPipeline` class and implement the abstract methods:

- **`getPipelineName()`**: Returns the name of the pipeline.
- **`getProcessor()`**: Returns the processor to handle file processing.
- **`sourceFolder()`**: Returns the path of the source folder to watch.

You can also override the following methods to customize folder paths and processing settings:

- **`processingFolder()`**: Returns the path of the folder for files in progress (default: "inprogress").
- **`destinationFolder()`**: Returns the path of the folder for processed files (default: "processed").
- **`errorFolder()`**: Returns the path of the folder for files with errors (default: "error").
- **`fileExtension()`**: Returns the file extension to watch for (default: null, meaning all files).
- **`splitter()`**: Returns the splitter for chunking files (default: "\n").
- **`chunkSize()`**: Returns the chunk size for splitting files (default: 0, meaning no chunking).

### Error handling
If an error occurs during file processing, the file will be moved to the error folder defined by method `errorFolder()`.

### Duplicate file names
If the destination folder already contains the same file name, pipeline will auto rename the existing file with current timestamp.

## Running the Application
Run your Spring Boot application. The pipeline will start according to property: `pipelines.start`