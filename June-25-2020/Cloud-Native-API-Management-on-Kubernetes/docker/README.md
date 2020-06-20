# Building the docker image of the Banking Services

1. Build the [bank-services](../Hospital-Service)
   ```
   cd ../bank-services
   mvn clean install
   ```

2. Copy the `workshop-0.0.1.jar` to directory where you have the `Dockerfile`
   ```
   cp target/workshop-0.0.1.jar ../docker/
   ```

3. Build the docker image
   ```
   cd ../docker
   docker build -t knightbeat/banksvc:1.0.1 .
   ```

4. (Optional) To tryout locally on Docker
   ```
   docker run -d -p 8080:8080 knightbeat/banksvc:1.0.1
   ```

5. (Optional) http://localhost:8080/core-banking/customers

6. (Optional) To stop the container
    ```
    docker stop $(docker ps -q --filter ancestor=knightbeat/banksvc:1.0.1)
    ```

7. (Optional) To delete the containers of this image
    ```
    docker rm $(docker ps -aq --filter ancestor=knightbeat/banksvc:1.0.1)
    ```

8. Push the docker image to Docker Hub
   ```
   docker push knightbeat/banksvc:1.0.1
   ```

9. Deploy on Kubernetes
    ```
    cd ..
    kubectl apply -f kubernetes/bank-services-deployment.yaml
    ```

10. To access the service externally
    * with port-forward option
        ```
        kubectl port-forward svc/banksvc 8080:8080
        ```
    * OR with EXTERNAL-IP option
        ```
        kubectl delete svc banksvc
        ```
        ```
        minikube tunnel
        ```
        ```
        kubectl expose deployment banksvc --type=LoadBalancer --port=8080
        ```
        Then you will get an External IP address to access the deployed service
        ```
        CK» kubectl get svc                                                  
        NAME              TYPE           CLUSTER-IP       EXTERNAL-IP      PORT(S)                         AGE
        banksvc           LoadBalancer   10.100.179.236   10.100.179.236   8080:32334/TCP                  8s
        ```
    * Change the `etc/hosts` accordingly 

#### Invocation examples

##### Get all credit customers
GET http://localhost:8080/core-banking/customers

##### Get credit customer by ID
GET http://localhost:8080/core-banking/customers/301091723

##### Submit Credit Application
```
POST http://localhost:8080/credit-service/applications
Content-Type: application/json

{
    "customerId":"301083909",
    "requestedAmount":2000,
    "creditType":"TERM_LOAN"
}
```

The **creditType** above can only be either `TERM_LOAN` or `OVERDRAUGHT`

##### Submit a credit proposal to the 'Central Credit Bureau'
```
POST http://localhost:8080/bureau/credit/proposals
Content-Type: application/json

{
    "id":"SMVZC2V5",
    "firstName": "Jessey",
    "lastName": "Pinkman",
    "address": "34 Wales Street Rothwell NN14 6JL",
    "requestedLoanAmount":2000,
    "yearsLivedInUK":3
}
```

##### Invoke 'local credit service' application to evaluate outstanding and issue overdraughts
```
POST http://localhost:8080/accounts-credit-service/overdraught
Content-Type: application/json

{
    "creditApplicationReference":"SMVZC2V5",
    "customerFisrtName": "Jessey",
    "customerLastName": "Pinkman",
    "phoneNumber": "+447720821292",
    "requestedAmount":2000,
    "customerId":"301083909"
}
```