FROM maven:3.6.1-jdk-8-alpine as backend-build
WORKDIR /career-center-api

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src src
RUN mvn install -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
ARG DEPENDENCY=/career-center-api/target/dependency
COPY --from=backend-build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=backend-build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=backend-build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.careercenter.CareerCenterApplication"]