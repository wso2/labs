# What is this lab kit ?

This lab kit contains the resource that we used in Managing full API lifecycle webinar

# Endpoints

- Production : http://apis.wso2.com:8000/mobile-service
- Sandbox : http://apis.wso2.com:9000/mobile-service

# Demo

We will demonstrate the following scenario during the webinar

- Show the API definition
    - Explain the current stage of the project
    - Show the example that are provided in the API definition by the API developer
- Goto Publisher
    - Click on Create API dropdown
    - Select `I Have a REST API`
    - Provide the OAS definition of the API (https://raw.githubusercontent.com/tmkasun/labs/master/June-17-2020/Managing-the-Full-API-Lifecycle/APIDefinitions/MobileStore-0.9.yaml)
    - Give the context of the API as `/mobile-store` and click `CREATE`
    - User will be redirected to the newly created API and you can see it’s in now in the CREATED state
- Let’s go to the *Endpoints page* and see what are the options available there,  
    - Goto Endpoints section and select `Prototype Implementation`
- Then Goto *Lifecycle page* and show the current state of the API (CREATED)
    - Then goto the Devportal to show the status there (https://apis.wso2.com/devportal/apis)
- Before promoting the API lifecycle state
    - API Developer needs to add documentation
        - Give document title as
            ```
            Mobile Store API Documentation
            ```
        - Give document summary as
            ```
            This document describes what are the available operations in this API and how to get the access and information that you need to get started
            ```
        - Use the document content is in here https://raw.githubusercontent.com/tmkasun/labs/master/June-17-2020/Managing-the-Full-API-Lifecycle/docs/sample.md
- Now go back to API *Lifecycle page* 
    - And promote the state to `PROTOTYPED`
- Now the prototyped API is running smoothly
    - Switch back to *Developer portal* and demonstrate
        - Invoking the prototyped API (Will keep it running for a while)
- Meanwhile we receive the production/sandbox endpoints for the API
- So Let's create a new version from the prototyped API and add the production and sandbox URLs
    - But still, App developers can continue using the prototyped API
- Click on `Create New Version`
- Give new version as `1.0.0`
- Show the current lifecycle state in *CREATED*
- Now Goto *Endpoints page* and change the endpoint type to HTTP/REST
    - Add 
        - Production : http://apis.wso2.com:8000/mobile-service
        - Sandbox : http://apis.sandbox.wso2.com:9000/mobile-service
            Endpoints
- Goto Subscriptions page
    - Select some business plans
- Go back to Lifecycle page
        - And promote to Publish state
- Now you will only see the latest version of MobileStore API
    - But you can still invoke the previous(prototyped) version of the API
- Open the Developer portal window
    - Click *Applications* -> *Add New Application*
        - Give the name as `Altronica Mobile Store App`
    - Go back to the API
        - Click on `Try-Out` menu
        - And try out the API request and lets keep it running for a while
- Now the API is running smoothly and every thing seems good
    - But just like in our lives, There are ups and down in the API's life as well
    - It's not like sailing in the deep quite see
    - So in the Altronica environment also , They found a major security vulnerability and their security department urges API Manager team to shutdown the API for all the users
    - That means, They need to block incoming API request from the Mobile application and they should block new subscriptions for new Applications in the developer portal
    - We have a API lifecycle state to manage this kind of crisis situation
    - API Developer can put the API into `BLOCKED` state to prevent API request forwarding to backend and block new subscriptions as well
    - Once the issue is fixed we can revers the state back to `PUBLISHED` state
- Now the bushiness has grown and come up with new requirement to add new Operation
    - We will create a new version out of 1.0.0
    - and deprecate the old version when publishing the `new 2.0.0` API
    - https://raw.githubusercontent.com/tmkasun/labs/master/June-17-2020/Managing-the-Full-API-Lifecycle/APIDefinitions/MobileStore-2.0.0.yaml
    - Will show the subscription migration in dev portal
    - And show the impact on API calls to deprecated API
- Then we will give some time for developers to migrate to the new version
    - And will retire the version 1.0.0 as it reach to the end of it's lifecycle
