# Processing-Data-Efficiently-With-WSO2-Enterprise-Integrator

This Project Include 2 scenarios.

1) Streaming Large Files with Integrator
2) Fetching account details from CSV files and updating the sales force

### Setting up

1) Clone the Project 
```bash
git clone https://github.com/wso2/labs.git
```
2) Open up the Integration Studio and import the project **ProcessingDataEfficientlyWithEI** to it

[Integration Studio Download](https://wso2.com/integration/integration-studio/)

[How To Import a Project to Studio](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/importing-projects/)

### Demo 1: Streaming Large Files with Integrator

This scenario will explain how you can transfer large files using enterprise integrator.

### Demo 2: Updating SalesForce Account details from CSV File records

This scenario will take the CSV file as input and upload its content to the sales force.

##### Artifacts

###### FileSplitApi

The API, which Used to split the large CSV file using File Connector into smaller files using predefined no. of lines

####### Request
```json
{
    "csv": "CSV_FILE_LOCATION",
    "targetFolder": "FOLDER_PATH_TO_CREATE_SPLITTED_FILES"
}
```

###### CSVToSalesForce

The proxy service which is used to poll the csv files from a folder and transform the csv to json format expected by 
the sales force and updates the sales force records.

###### TransformPaloadSeq

Transforms CSV to JSON

###### SalesForceUpdateSeq

Updates records to sales force
