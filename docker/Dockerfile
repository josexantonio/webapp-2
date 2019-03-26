FROM openjdk:8
COPY webapp-2.java /usr/src
COPY files /usr/src/files
COPY wait-for-it.sh /usr/src
WORKDIR /usr/src
CMD ["java", "-jar", "webapp-2.java"]