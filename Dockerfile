
FROM java:8

MAINTAINER spike19820318@gmail.com

COPY target/*.jar /opt/

CMD ["java", "-jar", "opt/yysports_item_service-1.0.1-SNAPSHOT.jar"]
