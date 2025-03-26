# --- 1단계: Maven으로 빌드하는 단계 ---
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# 의존성 먼저 다운로드
COPY pom.xml .
RUN mvn dependency:go-offline

# 소스 복사 후 빌드
COPY src ./src
RUN mvn clean package -DskipTests

# --- 2단계: Tomcat에 배포하는 단계 ---
FROM tomcat:10-jre17-temurin

# 빌드된 WAR 파일을 Tomcat의 webapps 디렉토리에 복사
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
