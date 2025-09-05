# 1단계: Gradle을 사용하여 애플리케이션 빌드 (Builder Stage)
# Java 24 JDK가 포함된 공식 Eclipse Temurin 이미지를 사용합니다.
FROM eclipse-temurin:24-jdk AS builder

# 작업 디렉토리 설정
WORKDIR /workspace/app

# Gradle 래퍼와 빌드 스크립트를 먼저 복사하여 Gradle 의존성을 캐싱합니다.
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# 필요한 의존성을 다운로드합니다.
# 소스 코드 없이 의존성만 먼저 다운로드 (빌드 속도 향상을 위함)
RUN ./gradlew dependencies

# 전체 소스 코드를 복사합니다.
COPY src ./src

# ★★★★★ 이 줄을 추가하세요! ★★★★★
# gradlew 파일에 실행 권한(x)을 부여합니다.
RUN chmod +x ./gradlew

# 애플리케이션을 빌드하여 .jar 파일을 생성합니다. (-x test로 테스트는 생략)
RUN ./gradlew build -x test

# 2단계: 실제 실행을 위한 최소한의 이미지 생성 (Final Stage)
# JDK가 아닌 JRE(Java Runtime Environment)만 포함된 더 가벼운 이미지를 사용합니다.
FROM eclipse-temurin:24-jre

# 작업 디렉토리 설정
WORKDIR /app

# Builder 스테이지에서 생성된 .jar 파일을 최종 이미지로 복사합니다.
# build/libs/ 폴더에 생성된 .jar 파일의 실제 이름을 확인하고 맞춰주세요.
COPY --from=builder /workspace/app/build/libs/*.jar app.jar

# 컨테이너 외부에서 8080 포트로 접근할 수 있도록 노출합니다.
EXPOSE 8080

# 컨테이너가 시작될 때 이 명령어를 실행하여 애플리케이션을 구동합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]