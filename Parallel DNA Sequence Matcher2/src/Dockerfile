FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy source files
COPY . /app

# Download JUnit JAR
ADD https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.3/junit-platform-console-standalone-1.9.3.jar /app/

# Compile all .java files with JUnit in classpath
RUN javac -cp .:junit-platform-console-standalone-1.9.3.jar *.java

# Optionally run tests
# RUN java -jar junit-platform-console-standalone-1.9.3.jar --class-path . --scan-class-path

ENTRYPOINT ["java", "DNAMatcherCLI"]
