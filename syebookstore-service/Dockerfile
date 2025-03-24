FROM openjdk:21

ENV JAVA_OPTS=""

WORKDIR /opt/service

COPY target/lib /opt/service/lib/
COPY target/syebookstore-service-1.0.jar /opt/service/syebookstore.jar

ENTRYPOINT ["/bin/sh", "-c", "java $JAVA_OPTS -cp /opt/service/syebookstore.jar:/opt/service/lib/* io.syebookstore.ServiceRunner"]
