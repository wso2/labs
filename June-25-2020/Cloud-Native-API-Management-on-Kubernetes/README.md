# Cloud-native API Management on Kubernetes

This is the companion repository for the [Cloud-Native API Management on Kubernetes (workshop) - EMEA/BST](https://wso2.com/events/workshops/2020-june-emea-cloud-native-api-management-on-kubernetes/)

## Pre-requisites

### Setting up basic environment

You should have following installed if you're following along the workshop. This is written in June 2020. If you're trying this out later, please be mindful that following links might not work. You might have to get the correct download link from theer respective home pages. If you have a working kubernetes cluster you can go to step #6.

1. Install Docker
   * [MacOS](https://hub.docker.com/editions/community/docker-ce-desktop-mac/)
   * [Windows](https://hub.docker.com/editions/community/docker-ce-desktop-windows/)
   * [Linux (Ubuntu 20.04)](https://linuxconfig.org/how-to-install-docker-on-ubuntu-20-04-lts-focal-fossa)
   * Linux (Ubuntu)
      * Uninstall older versions

         ```sh
         sudo apt-get remove docker docker-engine docker.io containerd runc
         ```

      * Making apt use repos over HTTPS

         ```sh
         sudo apt-get update
         sudo apt-get install \
               apt-transport-https \
               ca-certificates \
               curl \
               gnupg-agent \
               software-properties-common
         ```

      * Add Docker's offical PGP key

         ```sh
         curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
         ```

      * Add repo

          ```sh
          sudo add-apt-repository \
              "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
              $(lsb_release -cs) \
              stable"
          ```

      * Update repo index and install latest docker version

         ```sh
         sudo apt-get update
         sudo apt-get install docker-ce docker-ce-cli containerd.io
         ```

2. Install VirtualBox - [https://www.virtualbox.org/wiki/Downloads](https://www.virtualbox.org/wiki/Downloads)

3. Install minikube - [https://kubernetes.io/docs/tasks/tools/install-minikube/](https://kubernetes.io/docs/tasks/tools/install-minikube/)
   * Mac
      * via [Homebrew](https://brew.sh/)

         ```sh
         brew install minikube
         ```

      * Direct download and install

         ```sh
         curl -Lo minikube https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64 && chmod +x minikube
         sudo mv minikube /usr/local/bin
         ```

      * Windows - [Direct download](https://github.com/kubernetes/minikube/releases/latest). Download Windows executable and rename it to minikube.exe
      * Linux - Direct download

         ```sh
         curl -Lo minikube https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64 && chmod +x minikube
         sudo mkdir -p /usr/local/bin/
         sudo install minikube /usr/local/bin/
         ```

4. Install `kubectl` - [https://kubernetes.io/docs/tasks/tools/install-kubectl/](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
5. Start minikube

   ```sh
   minikube start --cpus=4 --memory=6144m
   ```

At this point you should have a working kubernetes cluster. Following steps should be executed once you have a working kubernetes cluster. Minikube provide a great, repeatable way to create a kubernetes cluster if you're just starting up.

### Setting up WSO2 Workshop Specifics

1. Install **WSO2 Integration Studio** - [https://wso2.com/integration/integration-studio/](https://wso2.com/integration/integration-studio/). We'll be using Integration Studio to demonstrate building complex integration services

2. Install **Kubernetes Operator for WSO2 Enterprise Integrator** (EI) [https://ei.docs.wso2.com/en/latest/micro-integrator/setup/deployment/kubernetes_deployment/](https://ei.docs.wso2.com/en/latest/micro-integrator/setup/deployment/kubernetes_deployment/)

3. Install **Kubernetes Operator for WSO2 API Manager** - [https://apim.docs.wso2.com/en/latest/learn/kubernetes-operators/k8s-api-operator/](https://apim.docs.wso2.com/en/latest/learn/kubernetes-operators/k8s-api-operator/)

4. Deploy sample Backend Service

   _This workshop excercises consist of a few case where we develop Integration Services by consuming some service endpoints that represent the existing Backend services, APIs and systems. Please follow the given instructions to deploy this and make sure that it is up and running._

   * Go to the [/bank-services](/June-25-2020/Cloud-Native-API-Management-on-Kubernetes/bank-services) directory of this repository.
   * Follow the instruction given in the `README.md`
   * You will also require a REST Client tool to test the APIs. I am using [VSCode IDE](https://code.visualstudio.com/) having [RESTClient](https://marketplace.visualstudio.com/items?itemName=humao.rest-client) installed on it. You can use the provided [vscode_rest-client.http](/June-25-2020/Cloud-Native-API-Management-on-Kubernetes/vscode_rest-client.http) with it easily.

## Part 1 - Develop Integration services

We'll be using WSO2 Integration Studio to create some integration services with the below given 3 scenarios to demonstrate development, testing & debug cycle.

1. A simple proxy API
    * This will proxy an existing service endpoint.
2. Using a **Connector**
    * This will fetch a File from an Amazon S3 bucket.
    * If you want to try this out, you may can create a **free-tier account** on Amazon.
3. Complex integration service that orchestrates a several service endpoints.
    * Use the provided backend service call [Bank Services](/June-25-2020/Cloud-Native-API-Management-on-Kubernetes/bank-services) on this repository.
    * Invoke the bank services endppints.
    * Walkthoiugh the complex integration service.
    * Testing a complex integration.
    * For testing to work, setup a hosts entry in your local machine - `banksvc`

**Note:** To test these, use the provided [vscode_rest-client.http](/June-25-2020/Cloud-Native-API-Management-on-Kubernetes/vscode_rest-client.http) file with VSCode IDE.

## Part 2 - Buld & Push a Docker Image of Integration Artefacts

1. Using WSO2 Integration Studio.
   * Using Kubernetes Exporter project `Build and Push Docker Image` option.
   * Or, using docker build command in the project directory (../CNAk8sExporter).

      ```sh
      docker build -t knightbeat/cna-integration:1.1.0 .
      docker push knightbeat/cna-integration:1.1.0
      ```

   * Note: `knightbeat` is my dockerhub repository. Change this according to yours.

## Part 3 - Deploy the Integration services into a kubernetes cluster

1. Deploy integration service into K8s (EI Operator)

   ```sh
   kubectl apply -f integration_cr.yaml
   ```

2. To access the deployment externally you can follow 2 approaches
   * With Port forward

      ```sh
      kubectl port-forward svc/myintegration-service 8290:8290
      ```

   * OR re-expose the deployment with a Load balancer
      * Start minikube tunnel if you haven't started it already

         ```sh
         minikube tunnel
         ```

      * Re expose the deployment with a LoadBalancer

         ```sh
         kubectl expose deployment cna-integration-deployment --type=LoadBalancer --port=8290 --name=cna-integration-loadbalancer
         ```

      * List the services and observe that the deployment exposed with a LoadBalancer

         ```sh
         kubectl get svc
         ```

      * Use the listed `EXTERNAL-IP` of "cna-integration-loadbalancer" to access the Integration Service from your machine.

## Part 4 - Creating a managed API in kubernetes using the CLI

When installing the Kubernetes Operator, you downloaded `k8s-api-operator-1.1.0.zip`. The following sample services and swagger files are available in that package. Therefore, please execute these commands of the extracted `k8s-api-operator-1.1.0` directory.

### 4.1 Sample Backend Service

1. Deploy sample backend service on k8s

   ```sh
   kubectl apply -f scenarios/scenario-1/products_dep.yaml
   ```

2. Test backend service

   ```erlang
   GET http://<EXTERNAL-IP>:80/products
   ```

   ```erlang
   GET http://<EXTERNAL-IP>:80/products/301
   ```

### 4.2 Expose it as a managed API on k8s

To perform this, we will be using the `apictl` tool.

1. Expose sample microservice as managed API

   ```sh
   apictl add api -n online-store --from-file=scenarios/scenario-1/products_swagger.yaml --replicas=2
   ```

2. This will spin up an instances of WSO2 Micro Gateway on k8s, proxying the 'products service' above.
3. Try to invoke the API

   ```erlang
   GET https://<EXTERNAL-IP>:9095/store/v1.0.0/products
   ```

   This should prompt you with an error message as the Managed API is secured.

   ```json
   {
      "fault": {
      "code": 900901,
      "message": "Invalid Credentials",
      "description": "Invalid Credentials. Make sure you have given the correct access token"
      }
   }
   ```

   Leave it as is at this point.

### 4.3 Publish the same API on WSO2 API Manager

1. Add an environment called `k8s`

   ```sh
   apictl add-env -e k8s --apim https://wso2apim:32001 --token https://wso2apim:32001/oauth2/token
   ```

2. Initialize an API project with the same Swagger(OAS) file as Microgateway above.

   ```sh
   apictl init online-store --oas=./scenarios/scenario-1/products_swagger.yaml --initial-state=PUBLISHED
   ```

3. Import the API to WSO2 API Manager - Developer portal

   ```sh
   apictl import-api -f online-store/ -e k8s -k
   ```

4. Goto the [API Manager Developer portal](https://wso2apim:32001/devportal) and verfify that the API has been published.

### 4.4 Access the API with Credentials

1. Generate keys with `apictl`

   ```sh
   apictl set --token-type JWT
   ```

2. Generate an Access-Token with `apictl` to invoke the API

   ```sh
   apictl get-keys -n online-store -v v1.0.0 -e k8s --provider admin -k
   ```

3. Invoke the API with Access-Token.

   ```erlang
   GET https://<EXTERNAL-IP>:9095/store/v1.0.0/products
   Authorization: Bearer <ACCESS-TOKEN>
   ```

   You should get a response like this

   ```http
   HTTP/1.1 200 OK
   content-type: application/json
   date: Mon, 22 Jun 2020 13:05:18 GMT
   server: ballerina/0.991.0
   connection: close
   content-encoding: gzip
   transfer-encoding: chunked

   {
      "products": [
         {
            "name": "Apples",
            "id": 101,
            "price": "$1.49 / lb"
         },
         {
            "name": "Macaroni & Cheese",
            "id": 151,
            "price": "$7.69"
         },
         {
            "name": "ABC Smart TV",
            "id": 301,
            "price": "$399.99"
         },
         {
            "name": "Motor Oil",
            "id": 401,
            "price": "$22.88"
         },
         {
            "name": "Floral Sleeveless Blouse",
            "id": 501,
            "price": "$21.50"
         }
      ]
   }
   ```

Alternatively, you can create an Application on the Developer Portal with Token type `JWT` and generate an Access-Token with that.

## Part 5 - CI/CD pipeline considerations / planning

### 5.1 Setting up two deployment environments

1. Install 2 copies of WSO2 API Manager 3.1.0 with one set to port offset to 1.
   * Modify _wso2am-3.1.0/repository/conf/deployment.toml_
      * Uncomment offset parameter and set if to 1 `offset=1`
   * This will allow running 2 WSO2 API Manager instances running on the same machine without port conflicts.

2. Using `apictl`, register those instances as two environments

   ```sh
   apictl add-env -e dev --apim https://localhost:9443 --token https://localhost:9443/oauth2/token
   apictl add-env -e prod --apim https://localhost:9444 --token https://localhost:9444/oauth2/token
   ```

### 5.2 Deploy an API on the 'dev' environment

1. Create and publish an API.
   * Load the [Publisher portal](https://localhost:9443/publisher) of the `dev` environment.
   * Use [https://petstore.swagger.io/v2/swagger.json](https://petstore.swagger.io/v2/swagger.json) to create the API.

### 5.3 Export API from the 'dev' environment

1. Use `apictl` export the `SwaggerPetstore` API.

   ```sh
   apictl export-api -e dev -n SwaggerPetstore -v 1.0.5 --provider admin
   ```

2. Unzip the contents of the downloaded _SwaggerPetstore-1.0.5.zip_ file.

### 5.4 Create and API project

1. Initialize an API project called `PetstoreAPI` with the _Swagger YAML_ file of the exported API.

   ```sh
   apictl init PetstoreAPI --oas SwaggerPetstore-1.0.5/Meta-information/swagger.yaml
   ```

2. Prepare the project for CI/CD, with endpoints in `api_params.yaml` in the _PetstoreAPI_ project.

   ```yaml
   environments:
   - name: dev
      endpoints:
         production:
            url: 'https://petstore.swagger.io/v2'
         sandbox:
            url: 'https://sandbox.petstore.swagger.io/v2'
   - name: prod
      endpoints:
         production:
            url: 'https://dev.petstore.swagger.io/v2'
         sandbox:
            url: 'https://dev.sandbox.petstore.swagger.io/v2'
   ```

   Other paramneters that you can change. (An example is given below)

   ```yaml
   environments:
     - name: <environment_name>
      endpoints:
          production:
              url: <production_endpoint_url>
              config:
                  retryTimeOut: <no_of_retries_before_suspension>
                  retryDelay: <retry_delay_in_ms>
                  factor: <suspension_factor>
          sandbox:
              url: <sandbox_endpoint_url>
              config:
                  retryTimeOut: <no_of_retries_before_suspension>
                  retryDelay: <retry_delay_in_ms>
                  factor: <suspension_factor>
      security:
         - enabled: <whether_security_is_enabled>
          type: <endpoint_authentication_type_basic_or_digest>
          username: <endpoint_username>
          password: <endpoint_password>
      gatewayEnvironments:
         - <gateway_environment_name>
      certs:
         - hostName: <endpoint_url>
          alias: <certificate_alias>
          path: <certificate_file_path>
   ```

### 5.5 Import API to the 'prod' environment

1. Load the [Publisher portal](https://localhost:9444/publisher) of the `prod` environment.
   * Observe that the SwaggerPetstore API is not there.

2. Use `apictl` import the API to the `prod` environment

   ```sh
   apictl import-api -f ./PetstoreAPI -e prod --preserve-provider=false --update=true
   ```

3. Go back tp the [Publisher portal](https://localhost:9444/publisher) of the `prod` environment.
   * Now you will see that the SwaggerPetstore API is created.
