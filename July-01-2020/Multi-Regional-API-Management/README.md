# What is this lab kit ?

The guides and related resources used for the sample setup which was demonstrate in this webinar, are uploaded in 
this lab kit.
The guide to setup a sample setup similar to the demo is described below.
 
 
 # Demo Guide
 
 ### Step 1 - API Manager Deployment
 
 1. Configure a 2 node active-active all in one WSO2 API-M deployment.
 
    ![Deplyment Diagram](/July-01-2020/Multi-Regional-API-Management/docs-artifacts/deployment-diagram.png)
    
    - According to the diagram there are multiple instances used for each server.
    - Please note that this deployment is used to demonstrate the multi-regional concept of API Management only. 
    - For more information on how to configure an active-active deployment please follow [Configuring an Active-Active Deployment](https://apim.docs.wso2.com/en/latest/install-and-setup/setup/single-node/configuring-an-active-active-deployment/#configuring-an-active-active-deployment)
    - NginX Load balancer is used to,
        - load balance the traffic to API-M-1 and APIM-2
        - load balance the traffic to each Region for API invocation calls (on Micro Gateway)
            - APAC region: abc-bank-rg1mg.apim.com
            - USA region: abc-bank-rg2mg.apim.com
        - set reverse proxy so that portals on 9443 port can be directly accessed from the LB cofigured url itself. (this
         is an optioal step)
                
    - For more information on how to configure load balancer, please refer [Configuring the Proxy Server and the Load Balancer](https://apim.docs.wso2.com/en/3.1.0/install-and-setup/setup/setting-up-proxy-server-and-the-load-balancer/configuring-the-proxy-server-and-the-load-balancer/)
    - The IP addresses ad the hostnames of of each instance/server is mentioned below, so that it will be convenient to 
    understand the deployment.
    - It is required to configure and install appropriate SSL certificates, in API-M Keystore and Trustore, since you will 
    be using custom hostnames (other than localhost) to access API-M endpoints. Please follow [Changing the Hostname](https://apim.docs.wso2.com/en/3.1.0/install-and-setup/setup/deployment-best-practices/changing-the-hostname/) for more 
    information.
        - Keystore, Trustore and the Certificate file used in the webinar are uploaded into (APIM-Keystore-Trustore)[/July-01-2020/Multi-Regional-API-Management/APIM-Keystore-Trustore]. A key-pair was generated
         in the keystore with CN=*.apim.com and the related public key certificate is self signed and installed into the Truststore 
         client-truststore.jks
    
    ###### Region : APAC
     - **2 WSO2 API-M 3.1.0 instances**
    
        - 192.168.114.25  : abc-bank.apim.com(LB hostname)
        - 192.168.114.55  : abc-bank.apim.com(LB hostname)
    
    - **MySQL 5.7 Database Server**
        - 192.168.114.62  : db.apim.com
    
    - **2 WSO2 Micro Gateway 3.1.0 Instances** (LB hostname: abc-bank-rg1mg.apim.com)
        - 192.168.114.56  : mg1.apim.com    [we will call this as APAC-MG1 in this guide]
        - 192.168.114.61  : mg2.apim.com    [we will call this as APAC-MG2 in this guide]
    
    ###### Region : USA
    
    - **2 WSO2 Micro Gateway Runtime Instances** (LB hostname: abc-bank-rg2mg.apim.com)
        
        - 192.168.114.63  : mg3.apim.com    [we will call this as US-MG1 in this guide]
        - 192.168.114.66  : mg4.apim.com    [we will call this as US-MG2 in this guide]

2. Adding Micro gateway labels

    2.1. Access the Admin portal of API Manager via https://abc-bank.apim.com/admin
    
    2.2. Sign in to the Admin Portal with an admin user.
    
    2.3. Click LABELS under MICROGATEWAY, and then click ADD MICROGATEWAY.
    
    2.4. Create a new label, add a host, and click Save.
        - We have added below labels in this sample
        
        | Label         | Gateway Host              | 
        | ------------- |:-------------------------:|
        | APAC-Region   | abc-bank-rg1mg.apim.com   | 
        | US-Region     | abc-bank-rg2mg.apim.com   | 
            
3. Create required APIs in the Publisher

    - We have created 2 APIs in this demo.
        1. ExchangeRatesAPI
        2. LoanDetailsAPI
    - Swagger files of the APIs we have created can be found in [API-Definitions](/July-01-2020/Multi-Regional-API-Management/API-Definitions)  

4. Assign the Microgateway label to an APIs created
    
    4.1  Login to the publisher and click on the ExchangeRatesAPI API to edit its configurations.
    
    4.2  Click on 'Environments'
    
    4.3  Select both Microgateway labels (APAC-Region, US-Region) you created. 
    
    4.4  Click on 'save'.
    
    4.5  In the same way assign only the ``US-Region`` label to LoanDetailsAPI.
    
        | API               | Labels                    | 
        | ------------------|:-------------------------:|
        | ExchangeRatesAPI  | APAC-Region, US-Region    | 
        | LoanDetailsAPI    | APAC-Region               | 

5.  Publish both the APIs and you can see that the labels are assigned to the APIs in Devportal
    - Access Devportal via http://abc-bank.apim.com/ and login.
    - The attached Microgateway labels will appear in the Overview tab of each API, under 'Available Environments'
    
### Step 2 - Micro Gateway Setup

Follow the instructions below to generate two Microgateway projects for the two Regions with APIs that correspond to 
each Microgateway label.

1. Install Microgateway Toolkit on your local machine. Please follow [Install on VM](https://docs.wso2.com/display/MG310/Install+on+VM) for more info.

2. Please note to import the certificates of the Load balancer, into the trustore of Microgateway Toolkit, since the 
toolkit needs to invoke API-M REST APIs to import APIs into a project.

3. Navigate to a preferred folder in your local machine, where you want to create the Microgateway project. 
Thereafter, run the following command.

    (command format : micro-gw init <project-name>)
    
         micro-gw init APAC-Region-Project

4. Import the APIs, which are published in WSO2 API Manager, that correspond to specific Microgateway label.

    (command format ``micro-gw import -l <label> <project-name>`` )
    
        micro-gw import -l APAC-Region APAC-Region-Project

    This command will import the 2 APIs ExchangeRatesAPI and LoanDetailsAPI into the project.
    After the APIs are imported, you can find the auto-generated OpenAPI definitions in the <MGW-project>/gen folder.

5. Build the WSO2 API Microgateway project ``APAC-Region-Project``

   (Command format : ``micro-gw build <project-name>``)

        micro-gw build APAC-Region-Project

    This creates an executable file (/APAC-Region-Project/target/APAC-Region-Project.jar) that you can use to expose the 
    group of APIs via WSO2 API Microgateway.

6. In the same way follow above 1-6 steps to create a Microgateway project (US-Region-Project) to the other label ``US-Region`` and to 
build a microgateway executable .jar (US-Region-Project.jar) for the label. 

    Projects and executable jar files which were used in this demo are uploaded into [Microgateway-Projects](/July-01-2020/Multi-Regional-API-Management/Microgateway-Projects).

7.  Start Microgateways

    7.1  Install Microgateway 3.1.0 Runtime in all the server instances dedicated for Microgateways. Please follow 
    [Install on VM - Microgateway Runtime](https://docs.wso2.com/display/MG310/Install+on+VM#InstallonVM-MicrogatewayRuntime) for more information.
    
    7.2  Note to add the public certificate of WSO2 API Manager to the microgateway trust store. For this,
     
       - Export public certificate of WSO2 API Manager.
       
            A sample command is given below.
            
                keytool -export -alias wso2carbon -file wso2carbon.crt -keystore <API-M_HOME>/repository/resources/security/wso2carbon.jks
    
       - Import the certificate using the command given below, to the trust store in the Microgateway runtime.
                
                keytool -import -trustcacerts -alias wso2carbon2 -file wso2carbon.crt -keystore <MGW_RUNTIME_HOME>/runtime/bre/security/ballerinaTruststore.p12
    
       - Open the <MGW_RUNTIME_HOME>/conf/micro-gw.conf file. Add the alias (e.g. wso2carbon2) to the truststore to corresponding configurations.
          For example, to configure the JWT authentication for API Manager JWTs, add the following JWT issuer configuration to the micro-gw.conf.
          
                [[jwtTokenConfig]]
                issuer = "https://aio.apim.com:443/oauth2/token"
                audience = "http://org.wso2.apimgt/gateway"
                certificateAlias = "wso2apim310"
                validateSubscription = false
                
       Please follow, [Configuring the Microgateway 3.1.x Runtime](https://docs.wso2.com/display/MG310/Configuration+for+WSO2+API+Manager#ConfigurationforWSO2APIManager-ConfiguringtheMicrogateway3.1.xRuntime) for more information on how to configure the Microgateway 3.1.0 Runtime.
       
    7.2  Transfer the built executables into the related Microgateway server instances.
    
    7.3  Login to the APAC-MG1 and run below command to start Microgateway instance in this server.
        (command format : gateway <path-to-MGW-executable-file>)
        ``gateway APAC-Region-Project.jar``
    7.4  In the same way, start Microgateways in the dedicated servers using the related executables you built.
        
        | Server        | Used executable           | 
        | ------------- |:-------------------------:|
        | APAC-MG1      | APAC-Region-Project.jar   | 
        | APAC-MG1      | APAC-Region-Project.jar   | 
        | US-MG1        | US-Region-Project.jar     | 
        | US-MG2        | US-Region-Project.jar     | 

8.  Now you have started the 4 Microgateways as expected and you can invoke APIs these Microgateways using a valid 
token.

### Step 3 - Subscribe and Invoke APIs

1. Login to the devportal and create an application. With below information.

        App name   : NewApp
        Token type : Self-contained (JWT)

2. Subscribe both APIs to the NewApp
3. Generate an access token for the application.
4. Invoke the APIs in separate Regions as using the generated access tokens.
- You will be able to invoke the LoanDetailsAPI from only abc-bank-rg1mg.apim.com LB-fronted MGs (in US Region) and 
you can invoke ExchangeRatesAPI from all the MGs.



