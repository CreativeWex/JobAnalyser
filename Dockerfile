FROM openjdk:17
COPY target/ClientMicroservice-0.0.1-SNAPSHOT.jar /usr/src/ClientMicroservice-0.0.1-SNAPSHOT.jar
ADD target/ClientMicroservice-0.0.1-SNAPSHOT.jar /usr/src/ClientMicroservice-0.0.1-SNAPSHOT/
WORKDIR /usr/src/ClientMicroservice-0.0.1-SNAPSHOT
ENTRYPOINT ["java","-jar","/usr/src/ClientMicroservice-0.0.1-SNAPSHOT.jar"]

