# SF Workshop, April 2023 - Companion Guide

For the SF workshop scheduled in April 2023, this guide serves as a companion to help you set up the services/APIs demonstrated during the event.
  
## Prerequisites

Before proceeding, there are a few prerequisites that your developer workstation should meet.
  1. The development process exclusively uses VSCode. You can download and install it from this link: https://code.visualstudio.com/
  2. As of now, Choreo doesn't support the latest Ballerina version, so you will need to download Ballerina 2201.4.1, which you can get from this link: https://ballerina.io/downloads/archived/#swan-lake-archived-versions. However, if you are trying this at a later date, Choreo might have been updated to support the latest version of Ballerina, so feel free to download and install the latest release
  3. ngrok - If you're running your services and database locally and want to connect from Choreo, download and use ngrok - https://ngrok.com/
  4. Sign up for Choreo - https://wso2.com/choreo
  5. Sign up for OpenAI an generate an API key - https://platform.openai.com/account/api-keys
  6. If you want to a GUI program to work with the database try - https://dbeaver.com. If you're comfortable with a specific DB and tools, that works

## Use case

During this workshop, we will implement a use case that involves generating a product catalog. We will use an Excel sheet that contains a few words describing the item and its characteristics. With the help of OpenAI's text and image generation APIs, we will generate a product description and an image, which we will save to a database. Subsequently, a separate API will retrieve the data from this database and convert the payload to JSON format. This API will then be utilized from a web application to display the product catalog.
Refer to the slides on system of records, domain APIs and experience APIs. For this use case the breakdown of services is something like below,

![service-breakdown](https://user-images.githubusercontent.com/57770159/233216888-fc21050d-0b84-48b0-b8e3-ccac74afcb4c.png)

The following outlines the use case we will be attempting during the workshop.,

![service-breakdown](https://user-images.githubusercontent.com/57770159/233217031-2329b4cd-6b62-4c3f-a43d-233bd6584f1c.png)

After deploying this use case to Choreo, you will see the implementation view of the Cell Based Architecture that was presented in the slides. It's important to note that you won't need to create the infrastructure explicitly. Choreo will automatically create cell boundaries based on the project you create from the UI.
![cell-based-view](https://user-images.githubusercontent.com/57770159/233217908-4d607658-9abb-42d5-828b-80b791d75ec5.png)

## Steps

1. Run and test the prices service. This is a simple HTTP service written in Go that will give item price.
2. Create a ballerina program using bal init <name>. You'll find the fully developed code under gsheet-trigger in this repo.
3. Use the HTTP connector and call the price service. Make sure to create a configurable variable for the endpoint because the endpoint will change once we deploy to Choreo.
4. Create a new file called Config.toml and use the same variable to pass the actual endpoint when testing out in your developer machine
5. Use ngrok to expose the ballerina service. We'll use this to test the Google Sheet trigger event
6. Follow the Google Sheet trigger creation guide here https://lib.ballerina.io/ballerinax/trigger.google.sheets/0.8.1. Create new Google Sheet and add the App Script. Use the endpoint you get from above step and replace the endpoint in the App Script
7. Add a new row the Google Sheet and test if you're getting the event response
8. Log the payload and copy it and paste to VSCode. Use Ballerina plugin feature Paste JSON as Record to create types. As demonstrated during the workshop you can remove almost all the values and just keep the value you really want. In our case it'll be newValues[][] attribute
9. Use the ballerina sequence diagram viewer to add the OpenAI text and image connectors
10. Call the text and image generation services to get a text response and image of the genearated URL
11. Use the sequence diagramming editor (the low-code developer view) of the Ballerina program to add the MySQL connector
12. Call the DB and execute insert statement
13. At this point you can create a project and host the Golang service and Google Sheet trigger service in Choreo. 
14. First deploy the golang service with Project scope for the endpoint. This will generate an endpoint that's only visible within the specific Project in Choreo (refer digarm above)
15. Deploy the Google Sheet trigger Ballerina program. Give the endpoint you get above
16. Create a new project in Choreo
17. Now create and depoly the API that talks to the DB and expose DB data as a JSON API. You can find this service in products-exp folder in this repo
