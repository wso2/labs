# What is this lab kit ?

This lab kit contains the resource that we used in `Moving Data Efficiently with Real-Time Streaming` webinar using WSO2 Streaming Integrator

## Pre-requisites
- Download the WSO2 Streaming Integrator Tooling from here https://wso2.com/integration/streaming-integrator/
- Download mysql-connector-java-x.x.x.jar compatible with the MySQL Server and copy it to {ServerHome}/libs

#### File Streaming into the Database scenario
- For scenario 1 with file processing, execute create database query `create database etl_demo;` in the July-08-2020/Moving-Data-Efficiently-with-Real-Time-Streaming/Artefacts/SQLScripts/file.sql
- Copy the July-08-2020/Moving-Data-Efficiently-with-Real-Time-Streaming/Artefacts/Siddhi-Apps/FileToDBSiddhiApp.siddhi to {ServerHome}/wso2/server/deployment/workspace
- Copy July-08-2020/Moving-Data-Efficiently-with-Real-Time-Streaming/Artefacts/ReceiveEventsFromFile folder into your machine and edit the given paths including `ReceiveEventsFromFile` in the file source to the absolute paths in your machine

#### CDC scenario
- abc

## Demo on File Streaming into the Database scenario
- This demo will elaborate how can a file content can be streamed via WSO2 Streaming Integrator to be moved to a database.
- While the processing happens, an aggregation will happen as well
- Play the Siddhi application
- There will be log similar to `[2020-07-08 22:00:07,792]  INFO {io.siddhi.core.query.processor.stream.LogStreamProcessor} - SiddhiApp: inserted: , StreamEvent{ timestamp=1594225807782, beforeWindowData=null, onAfterWindowData=null, outputData=[WSO2, 0, 10000], type=CURRENT, next=null}`, file streaming has started.
- Once the processing is over, goto the database console and check for the data using the following two commands 
`select * from SweetProductionTable;
 select * from SweetProductionAgg_SECONDS;` in July-08-2020/Moving-Data-Efficiently-with-Real-Time-Streaming/Artefacts/SQLScripts/file.sql
 
 ## Demo on CDC scenario





