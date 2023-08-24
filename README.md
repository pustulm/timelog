TUTORIAL FOR DEPLOYMENT OF TIME LOG GO APP

1. PREREQUISITES

-Docker
-Kubernetes (kubectl, minikube)
-GitHub account
-Jenkins (on Docker)

2. PULL REPOSITORY FROM GITHUB TO YOUR LOCAL MACHINE

To pull repo from my github acc just use 

```
git clone https://github.com/pustulm/timelog
```

3. DOCKERIZE YOUR APP

Navigate to cloned repository. There is already a `Dockerfile`

```
FROM golang:1.21.0-alpine3.17

WORKDIR /app

COPY . .

RUN go build -o main

EXPOSE 8000

CMD ["./main"]
```

I'm using golang in alpine version because of small size of created image.

In order to create image use following command 

```
docker build -t timelog-app .

```

To test if image is working create a container based on it:

```
docker run -d -p 8000:8000 timelog-app
```

You should be able to acces the API on 'http://localhost:8080'. If everything is all right you should see a time on your screen.

4. PUSH YOUR IMAGE TO DOCKER HUB

To push image to DockerHub you have to Sign Up to https://hub.docker.com/.

```
docker push YOUR-USER-NAME/timelog-app
```

5. SET UP KUBERNETES CLUSTER

Next step is creating a Kubernetes Deployment YAML file.

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: timelog-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: timelog-app
  template:
    metadata:
      labels:
        app: timelog-app
    spec:
      containers:
        - name: timelog-api
          image: your_dockerhub_name/timelog-app:TAG
          ports:
            - containerPort: 8000
````

You have to change image name to name of image that you uploaded to DockerHUB. When you set your YAML file use command 

```
kubectl apply -f timelog-deployment.yaml
```

6. SET MINIKUBE

