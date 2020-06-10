# Cloud-native API Management on Kubernetes

Companion repo for workshop https://wso2.com/events/workshops/2020-june-apac-cloud-native-api-management-on-kubernetes/

## Pre-requisites

You should have following installed if you're following along the workshop. This is written in June 2020. If you're trying this out later, please be mindful that following links might not work. You might have to get the correct download link from their respective home pages. If you have a working Kubernetes cluster you can go to step #6.

1. Docker - https://docs.docker.com/get-docker/
    * Mac - https://hub.docker.com/editions/community/docker-ce-desktop-mac/
    * Windows - https://hub.docker.com/editions/community/docker-ce-desktop-windows/
    * Linux (Ubuntu 20.04) - https://linuxconfig.org/how-to-install-docker-on-ubuntu-20-04-lts-focal-fossa
    * Linux (Ubuntu) 
        * Uninstall older versions 
          ```
          $ sudo apt-get remove docker docker-engine docker.io containerd runc
          ```
        * Making apt use repos over HTTPS
          ```
          $ sudo apt-get update
          $ sudo apt-get install \
                 apt-transport-https \
                 ca-certificates \
                 curl \
                 gnupg-agent \
                 software-properties-common
          ```
        * Add Docker's offical PGP key
          ```
          $ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
          ```
        * Add repo
          ```
          $ sudo add-apt-repository \
              "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
              $(lsb_release -cs) \
              stable"
          ```
        * Update repo index and install latest docker version
          ```
          $ sudo apt-get update
          $ sudo apt-get install docker-ce docker-ce-cli containerd.io
          ```
2. Install VirtualBox - https://www.virtualbox.org/wiki/Downloads
3. Install minikube - https://kubernetes.io/docs/tasks/tools/install-minikube/
    * Mac
        * via [Homebrew](https://brew.sh/) ```brew install minikube```
        * Direct download and install
          ```
          $ curl -Lo minikube https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64 && chmod +x minikube
          $ sudo mv minikube /usr/local/bin
          ```
    * Windows - [Direct download](https://github.com/kubernetes/minikube/releases/latest). Download Windows executable and rename it to minikube.exe
    * Linux - Direct download
       ```
       $ curl -Lo minikube https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64 && chmod +x minikube
       $ sudo mkdir -p /usr/local/bin/
       $ sudo install minikube /usr/local/bin/
       ```
4. kubectl - https://kubernetes.io/docs/tasks/tools/install-kubectl/
5. Start minikube - ```minikube start --cpus=4 --memory=6144m```

Note: You can have a working kubernetes cluster with WSL. [Follow this blog to setup WSL](https://dev.to/wxyz/playing-with-windows-subsystem-for-linux-wsl-hio). Install Docker for Windows after that.

At this point you should have a working Kubernetes cluster. Following steps should be executed once you have a working Kubernetes cluster. Minikube provide a great, repeatable way to create a Kubernetes cluster if you're just starting up.

1. Install WSO2 Integration Studio - https://wso2.com/integration/integration-studio/. We'll be using Integration Studio to demonstrate building complex integration services

2. Install EI Kubernetes Operator https://ei.docs.wso2.com/en/latest/micro-integrator/setup/deployment/kubernetes_deployment/

3. Install API Kubernetes Operator - https://apim.docs.wso2.com/en/latest/learn/kubernetes-operators/k8s-api-operator/

## Part 1 - Developing backend services

We'll be using Integration Studio to create some services and demonstrate develop, build, test, debug cycle.

1. With a very simple proxy service - invoking a mocky.io backend
2. Using a connector, get file from Amazon S3
    * Demo on develop, build, debug
3. Complex integration service that orchestrate between several microservices
    * Step by step instructions on setting up the sample - https://ei.docs.wso2.com/en/latest/micro-integrator/use-cases/tutorials/exposing-several-services-as-a-single-service/
    * Use my backend service - [hospital service](../Hospital-Service-Artifacts/Hospital-Service) that's found on this repo
    * Invoke healthcare service
    * Testing a complex integration
    * Building the maven multi-module project - The entire integration can be built using maven. Can easily be automated through Jenkins
    * For testing to work, setup a hosts entry in your local machine - hospitalsvc

## Part 2 - Deploying services (microservices and integration services) into a kubernetes cluster

1. Deploy backend services into K8S
   ```
   $ kubectl apply -f Hospital-Service-Artifacts/hospital-service-deployment.yaml
   $ kubectl port-forward svc/hospitalsvc 9091:9091
   ```
2. Deploy integration service into K8s (EI Operator)
   ```
   $ kubectl apply -f SampleServicesParentMMM/SampleServicesKubernetesExporter/integration_cr.yaml
   $ kubectl port-forward svc/myintegration-service 8290:8290
   ```

## Part 3 - Creating a managed API in kubernetes using the CLI

1. Deploy sample backend service
   ```
   $ kubectl apply -f scenarios/scenario-1/products_dep.yaml
   ```
2. Test backend service
   ```
   $ curl -X GET http://<EXTERNAL-IP>:80/products
   ```
3. Expose sample microservice as managed API
   ```
   $ apictl add api -n online-store --from-file=scenarios/scenario-1/products_swagger.yaml --replicas=3
   ```
4. Invoke the service
   ```
   $ curl -X GET "https://<EXTERNAL-IP>:9095/store/v1.0.0/products" -k
   ```
5. Push API to Developer Portal
   * Add an environment
     ```
     $ apictl add-env -e k8s --apim https://wso2apim:32001 --token https://wso2apim:32001/oauth2/token
     ```
   * Initialize the project
     ```
     $ apictl init online-store --oas=./scenarios/scenario-1/products_swagger.yaml --initial-state=PUBLISHED
     ```
   * Import API to API portal
     ```
     $ apictl import-api -f online-store/ -e k8s -k
     ```
 6. Double check on API Store
 7. Generate keys
    ```
    $ apictl set --token-type JWT
    ```
 8. Generate access token
    ```
    $ apictl get-keys -n online-store -v v1.0.0 -e k8s --provider admin -k
    ```
   
## Part 4 - CI/CD pipeline considerations / planning

1. Install 2 copies of WSO2 API Manager 3.1.0 with one set to port offset to 1 (so ports wont clash during startup).
2. Using ```apictl``` add 2 instances as 2 environments
   ```
   $ apictl add-env -e dev --apim https://localhost:9443 --token https://localhost:9443/oauth2/token
   $ apictl add-env -e prod --apim https://localhost:9444 --token https://localhost:9444/oauth2/token
   ```
3. Create and publish an API. Use https://petstore.swagger.io/v2/swagger.json for creating teh API. This represent the dev environment. Any environment with existing APIs (created by developers or by a CI process). Enter a dev endpoint.
4. Use ```apictl``` export the API, unzip the contents
   ```
   $ apictl export-api -e dev -n SwaggerPetstore -v 1.0.0 --provider admin
   ```
5. Initialize the project 
   ```
   $ apictl init PetstoreAPI --oas path/to/petstore.yaml
   ```
6. Preparing the project for CI/CD, change endpoints in api_params.yaml
   ```
   environments: 
    - name: dev 
    endpoints: 
      production: 
        url: 'http://dev.wso2.com' 
      sandbox:
        url: 'http://dev.sandbox.wso2.com' 
    - name: prod
    endpoints:
      production:
        url: 'http://prod.wso2.com'
      sandbox:
        url: 'http://prod.sandbox.wso2.com'
   ```
   What more can we change? Here's another example,
   ```
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
7. Import API
   ```
   $ apictl import-api -f ./SwaggerPetstore -e prod --preserve-provider=false --update=true
   ```
