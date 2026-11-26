FROM gradle:9.9.2-jdk21-corretto AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle --no-daemon kotlinUpgradeYarnLock jsBrowserDistribution fatJar

FROM gcr.io/distroless/java21-debian13:nonroot
WORKDIR /app
COPY --from=build --chown=nonroot:nonroot /home/gradle/src/card-trader-scout/build/libs/*-all.jar /app/app.jar
COPY --from=build --chown=nonroot:nonroot /home/gradle/src/card-trader-scout/build/dist/js/productionExecutable /app/webapp
USER nonroot
EXPOSE 8081
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
