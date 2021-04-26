FROM openjdk:15

ADD build/libs/cloud_storage-0.0.1-SNAPSHOT.jar cloudstorage.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/cloudstorage.jar"]