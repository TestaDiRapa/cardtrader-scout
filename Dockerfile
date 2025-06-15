FROM gradle:8.4.0-jdk21-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle fatJar

FROM amazoncorretto:21-alpine3.18
RUN mkdir /app
COPY --from=build /home/gradle/src/card-trader-scout/build/libs/*.jar /app/app.jar
COPY --from=build /home/gradle/src/card-trader-scout/build/dist/wasmJs/productionExecutable /app/webapp
ENTRYPOINT ["java","-jar","/app/app.jar"]