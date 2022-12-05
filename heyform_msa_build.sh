#!/bin/bash
if [ $# -ne 1 ]
then
    echo "usage: $0 <docker hub ID>"
    exit 1
fi

USER=$1

#list="config-server eureka-server api-gateway user-service survey-service answer-service"
list="survey-service"
for IMG in ${list}
do
  echo "===================================================================================================="
  echo "remove the existing images"
  echo "----------------------------s----------------------------------------------------------${IMG}"
  docker image rm ${IMG}
  docker image rm ${USER}/${IMG}
  echo "===================================================================================================="
  cd ./${IMG}
  pwd
  echo "----------------------------------------------------------------------------------------------------"
  echo "clean mvnw"
  chmod -R 777 ./target/
  ./mvnw clean package
  chmod -R 777 ./target/
  echo "----------------------------------------------------------------------------------------------------"

  echo "===================================================================================================="
  echo "make docker file"
  echo \
"FROM adoptopenjdk/openjdk11
CMD [\"./mvnw\", \"clean\", \"package\"]
ARG JAR_FILE=target/*.jar
COPY \${JAR_FILE} app.jar
ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]" \
  > Dockerfile
  chmod -R 777 Dockerfile
  echo "===================================================================================================="

  docker build --platform linux/amd64 -t ${IMG} .
#  docker build -t ${IMG} .
  docker tag ${IMG}:latest ${USER}/${IMG}:latest
  docker push ${USER}/${IMG}:latest
  cd ..
done

# ip 바꿔보기