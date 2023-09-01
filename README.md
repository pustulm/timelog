TUTORIAL FOR DEPLOYMENT OF TIME LOG GO APP

---OVERVIEW OF PROJECT---
The goal of this repository is to make a CI/CD job for a simple Time Log App which involves usage of basic DevOps tools like Jenkins or Kubernetes.

---STEPS---

1. PREREQUISITES

-Docker and DockerHub account
-Kubernetes (kubectl, minikube)
-GitHub account
-Jenkins 

2. PULL REPOSITORY FROM GITHUB TO YOUR LOCAL MACHINE

To pull repository from my github account just use:

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

You should be able to acces the API on 'http://localhost:8080'. If everything is all right you should see a log of time on your screen.

4. PUSH YOUR IMAGE TO DOCKER HUB

To push image to DockerHub you have to Sign Up to https://hub.docker.com/.

```
docker push YOUR-USER-NAME/timelog-app
```

5. SET UP KUBERNETES CLUSTER

Next step is creating a Kubernetes Deployment YAML file.

```
# timelog-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: time-log-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: time-log-app
  template:
    metadata:
      labels:
        app: time-log-app
    spec:
      containers:
        - name: timelog-app
          image: michalp96/timelog-app:latest
          resources:
            requests:
              memory: "64Mi"
              cpu: "250m"
            limits:
              memory: "128Mi"
              cpu: "500m"
          ports:
            - containerPort: 8000
---
apiVersion: v1
kind: Service
metadata:
  name: timelog-service
spec:
  selector:
    app: timelog-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8000
  type: LoadBalancer

```

You have to change image name to name of image that you uploaded to DockerHUB. When you set your YAML file use command 

```
kubectl apply -f timelog-deployment.yaml
```

6. SET MINIKUBE (OPTIONALLY IF YOU WANT RUN APP LOCALY)

To set minikube just type in console

```
minikube start --driver=docker
```

Type in console

```
minikube status
```

to check if everything is okay with your minikube.

If you want to connect to minikube use one of these commmands:

```
ssh -i ~/.minikube/machines/minikube/id_rsa docker@$(minikube ip)
```

or

```
minikube ssh
```

7. SETTING UP JENKINS

To run jenkins as docker container use this command

```
docker run -u root --privileged --name jenkins -d -p 8080:8080 -p 50000:50000 -v /var/run/docker.sock:/var/run/docker.sock -v $(which docker):/usr/bin/docker -v jenkins_data:/var/jenkins_home --network minikube jenkins/jenkins:lts
```
This command aside of creating a Jenkins container also with install Docker into it. 

Next step is installing all needed plugins:
-Kubernetes
-Kubernetes CLI
-Docker plugin
-Github plugin

8. ADDING ALL CREDENTIALS TO JENKINS

From the dashboard in Jenkins go to Manage Jenkins => Credentials => System => Global Credentials then Add Credentials. Next step is add credentials for your GitHub profile, and then do the same for the DockerHub. Then upload your kubeconfig file for kuberentes credentials. You can find it under location:
```
~/.kube/config
```
You may have to change the way of apperance of the certifcate and key inside kubeconfig. To do this type this command into CLI 
```
cat {put here location of certificate-authority} | base64 -w 0; echo
```
and then copy-paste result of this into kubeconfig file under certifcate-authority. Also add -data to the name of certificate-authority, so it should looki like this
```
certificate-authority-data:{YOUR certificate}
``` 
Repeat for the client certificate and client-key. Then upload again your kubeconfig to credentials of kubernetes.

9. CREATING CLOUD FOR JOB

Now go to the Manage Jenkins=>Clouds=>New Clouds
Name your cloud, choose the kuberenetes and then click Create.
Then upload your credentials for this cloud. Use the kubernetes ones.

10. CREATE JOB
Now you're ready to create the job. From the dashboard of jenkisn click New Item. Call your Job and use Multibranch Pipeline in this case. Then choose Branch Sources as github. Complete the credentials with your GitHub ones, and then paste a link to your repository on GitHub. Then apply and go to your job tab in jenkins. Scan repository now. If everything is fine you should get a information about this in Scan Repository Log. The pipeline from JenkinsFile from your github repository should be loaded and executed.

11. TESTS

You can check if everything went fine in kubectl. Try to get pods, deployments and service. You should be able to connect to created pods from minikube ssh.

---SUMMARIES OF THE PROJECT---
It was my frist DevOps project. It far from perfect, but I'm pretty happy about the result of my work. The biggest problem was configuring the pipeline in Jenkins. There was a lot of problems, most of them caused by lack of experience in working in containers. Also the part of pipeline which involved the kubernetes was really problematic. Until find the usage of the Kuberenetes CLI plugin I circled around. As matter of fact I really enjoyed finding the solutions of every problem that I met and solving it. I learn a lot of things in this simple project which will help in future projects.
In my next projects I want to use more of DevOps tools like Ansible or Helm. I would also like to try other CI/CD tools like GitHub Actions.

