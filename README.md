### Overview

This is a java based boilerplate which runs an HTTP Server configured to answer the endpoints defined in
[the challenge you received](https://backend-challenge.asapp.engineering/). All endpoints are configured in
src/main/java/com/asapp/backend/challenge/Application.java and if you go deeper to the Routes and Filters passed as second parameters, you will find a
TODO comment where you are free to implement your solution.

### How to run com.asapp.backend.challenge.it (locally)

```
./gradlew run
```

### How to build a docker image

```shell
./gradlew dockerBuildImage && docker tag com.asapp.backend.challenge/java-boilerplate:1.0 asapp/java-boilerplate
```

### How to run `deployment.yaml` for Kubernetes (_MiniKube_)

> **Note:** Need to run the previous step, to build and package the docker image being used in the `deployment.yaml`

Then run:
```shell
kubectl apply -f k8s/deployment.yaml          # Wait for the pods to be ready
kubectl port-forward com.asapp.backend.challenge.service/asapp 8080:8080
```

### How to check the project is running correctly

```shell
curl -k -d'{}' 127.0.0.1:8080/check    # Expected: { \"health\": \"OK\"}
```

### Resources

[How to Run Locally Built Docker Images in Kubernetes](https://medium.com/swlh/how-to-run-locally-built-docker-images-in-kubernetes-b28fbc32cc1d)
