## VisiDoc
<div style="text-align: center;">
    <img alt="shopshere_main" src="https://github.com/user-attachments/assets/bf3efd80-989e-4ac8-837d-9eff685e74c5" />
</div>

## 프로젝트 소개
#### "분산된 데이터베이스와 전문적인 의료 API를 어떻게 하나의 완벽한 웹 서비스로 통합할 수 있을까?" 이 질문이 바로 저희 VisiDoc의 핵심 과제였습니다. 

저희는 이미 웹에서 강력한 기능을 제공하는 PacsPlus를 벤치마킹하여, 최신 기술 스택으로 안정적인 의료 이미지 조회 시스템을 구축하는 것을 목표로 삼았습니다. 이를 위해 Spring Boot의 견고한 백엔드와 Next.js(React)의 유연한 프론트엔드를 선택했습니다.

이 프로젝트의 핵심은 단순히 기능을 구현하는 것을 넘어, 사내 서버의 Oracle DB와 클라우드의 AWS RDS를 동시에 연동하는 백엔드 아키텍처를 설계하고, Cornerstone.js API를 활용하여 전문적인 DICOM 뷰어를 웹상에 안정적으로 구현해내는 것이었습니다.

##### 사이트 주소: http://43.202.185.167/login  (클라우드 배포 버전, Oracle DB 연동 제외)
##### 설치 방법: <a href="/dosc/설치방법/README.md" target="_blank">설치방법</a>
<br>

### 주요 기능 (Key Features)
| 기능 | 설명 | 관련 기술 |
| :---: | --- | :---: |
| **보안 인증 및 인가** | Spring Security와 **JWT**(JSON Web Token)를 결합하여 견고한 인증/인가 시스템을 구현했습니다. 사용자의 역할(의료진)에 따라 접근 권한을 제어하여 민감한 의료 데이터를 안전하게 보호합니다.| `Spring Security`, `JWT`|
| **하이브리드 API 설계**| **GraphQL과 REST API를 목적에 맞게 함께 사용**하는 하이브리드 방식을 채택했습니다. 복잡하게 연관된 의료 데이터(환자-연구-시리즈) 조회는 **GraphQL**로, 의사 소견서 작성이나 이미지 파일 요청처럼 명확한 기능은 **REST API**로 처리하여 API를 최적화했습니다.| `GraphQL`, `REST API`, `Spring Boot`|
| **인터랙티브 DICOM 이미지 뷰어**| 전문 라이브러리인 **Cornerstone.js API를 활용**하여 DICOM 이미지를 렌더링하고, 필수 분석 도구(Zoom, Pan, Windowing 등)를 제공합니다. 또한, 실제 파일 경로를 숨기기 위해 **서버에서 디코딩 과정을 거쳐 이미지를 안전하게 스트리밍하는 보안 전송 메커니즘**을 REST API로 직접 구현했습니다. | `Cornerstone.js`, `REST API`, `Spring Boot`|
| **분산 데이터 통합 조회**| 학교 DB(Oracle)의 **환자/연구 정보**와 AWS RDS(PostgreSQL)의 **의사 소견 정보**를 백엔드에서 **실시간으로 통합**하여, 사용자에게는 하나의 완성된 정보처럼 보여주는 기능을 구현했습니다.  | `Spring Boot`, `JPA`, `JPQL`|
| **목적 중심의 데이터베이스 아키텍처** | **학교 서버(Oracle DB)의 PACS 연동 데이터**와 자체적으로 구축한 **AWS RDS(PostgreSQL)의 애플리케이션 데이터(의사 정보, 소견서 등)를 물리적으로 분리**하여 설계했습니다. 이를 통해 데이터의 성격에 따라 관리 포인트를 분리하고 시스템의 유연성을 확보했습니다.| `Multi-Datasource`, `Oracle DB`, `PostgreSQL` |

<br>
<br>

## 팀원 
|박규태|김동현|김윤진|
|:---:|:---:|:---:|
|<a href="https://github.com/ZaRi1l" target="_blank"><img src="https://avatars.githubusercontent.com/u/133009070?v=4" height="150px"/><br>ZaRi1l</a>|<a href="https://github.com/kimdonghyun296" target="_blank"><img src="https://avatars.githubusercontent.com/u/193192616?v=4" height="150px"/><br>kimdonghyun296</a>|<a href="https://github.com/yunndaeng" target="_blank"><img src="https://avatars.githubusercontent.com/u/193191038?v=4" height="150px"/><br>yunndaeng</a>|

#### 맡은 역할
| 이름 |업무|
|:---:|---|
|박규태|**프론트엔드**: 백엔드 API 연동 기반의 이미지 로딩 및 뷰잉 워크플로우 전체 구현, Cornerstone.js를 활용한 DICOM 이미지 뷰어 및 분석 도구(Zoom, Pan 등) 최종 개발 <br>**백엔드**: Multi-Datasource 아키텍처 설계 (Oracle & PostgreSQL 동시 연동), Native SQL 기반 Oracle DB 데이터 조회 로직 구현, 환자 정보 조회를 위한 GraphQL API 및 이미지 보안 전송 REST API 구축, Swagger API 문서화 <br>**데브옵스**: AWS 기반 인프라 구축 (EC2, RDS), Docker & Github Actions 기반 CI/CD 파이프라인 구축, Spring Boot 및 Next.js의 환경별(dev/prod) 구성 관리 <br>**기타**: 영상 편집, 프로젝트 리드미 작성, Class Diagram 작성|
|김동현|**프론트엔드**: REST/GraphQL API를 연동하여 사용자 인증 흐름(로그인, 회원 생성) 구현, 환자 검색(이름, ID, 날짜)부터 Study/Series로 이어지는 계층적 데이터 탐색(Drill-down) UI 및 소견서 작성/조회 기능을 포함한 핵심 워크플로우를 개발, React Context API를 활용한 전역 로그인 상태 관리 구현 <br>**백엔드**: PostgreSQL DB 설계 및 JPA를 활용한 데이터 CRUD 계층 전체 구현, JWT와 Spring Security를 활용한 회원 인증 REST API 구축, GraphQL Resolver를 통해 이종 데이터베이스(Oracle, PostgreSQL)의 데이터를 조합하는 소견서 작성 API 개발 <br>**기타**: 발표 자료 제작, 영상 촬영, Usecase Diagram 작성|
|김윤진|**프론트엔드**: 프로젝트의 전체 UX/UI 및 레이아웃 설계를 총괄, CSS Modules를 활용하여 로그인, 메인 대시보드, 뷰어, 관리자 페이지 등 모든 핵심 페이지의 UI 개발과 반응형 웹 디자인을 담당, DICOM 뷰어의 초기 기술 검증(PoC) 및 로컬 이미지 렌더링 프로토타입을 구현|

<br><br>
## 💻 개발 환경

| Backend | Frontend | Database | VCS | DevOps | CSP |
|:---:|:---:|:---:|:---:|:---:|:---:|
|<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="85" alt="Spring Boot"/>|<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg" width="85" alt="React"/> <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/nextjs/nextjs-original.svg" width="85" alt="Next.js"/>|<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/oracle/oracle-original.svg" width="85" alt="Oracle"/> <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="85" alt="PostgreSQL"/>|<img src="https://img.icons8.com/?size=100&id=SzgQDfObXUbA&format=png&color=000000" width="85" alt="GitHub"/>|<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" width="85" alt="Docker"/> <img src="https://cdn.simpleicons.org/githubactions/2088ff" width="85" alt="Github Actions"/> |<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/amazonwebservices/amazonwebservices-original-wordmark.svg" width="85" alt="AWS"/>|
|Spring Boot|React, Next.js|Oracle, PostgreSQL|GitHub|Docker, GitHub Actions|AWS|

| Language | IDE | Build Tool |
|:---:|:---:|:---:|
|<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="65" alt="Java"/> <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/typescript/typescript-original.svg" width="65" alt="TypeScript"/> <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/javascript/javascript-original.svg" width="65" alt="JavaScript"/> <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/html5/html5-original.svg" width="65" alt="HTML"/> <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/css3/css3-original.svg" width="65" alt="CSS"/>|<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/intellij/intellij-original.svg" width="85" alt="IntelliJ"/> <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/vscode/vscode-original.svg" width="85" alt="VSCode"/>|<img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/gradle/gradle-original.svg" width="85" alt="Gradle"/> <img src="https://img.icons8.com/color/96/npm.png" width="85" alt="NPM"/>|
|Java, TypeScript, JS, HTML, CSS|IntelliJ, VSCode|Gradle, NPM|

<br>

### ⚙️ 개발 환경 상세

#### Backend

| 구분 | 사용 기술 | 버전 |
|:---:|:---|:---:|
| **Framework** | <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/> | 3.5.4 |
| **Language** | <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white"/> | 24 |
| **API** | <img src="https://img.shields.io/badge/GraphQL-E10098?style=for-the-badge&logo=graphql&logoColor=white"/> <img src="https://img.shields.io/badge/REST%20API-000000?style=for-the-badge"/> | - |
| **Data Access** | <img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge"/> | - |
| **Security** | <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white"/> <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white"/> | - |
| **API Docs** | <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black"/> | 2.8.9 |
| **File Access** | <img src="https://img.shields.io/badge/JCIFS--NG-blue?style=for-the-badge"/> | 2.1.10 |
| **Build Tool** | <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"/> | - |

<br>

#### Frontend

| 구분 | 사용 기술 | 버전 |
|:---:|:---|:---:|
| **Framework**| <img src="https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=next.js&logoColor=white"/> | 15.4.6 |
| **Library** | <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black"/> | 19.1.0 |
| **Language** | <img src="https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white"/> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"/> | ^5 / ES6+ |
| **Data Fetching** | <img src="https://img.shields.io/badge/Apollo%20Client-311C87?style=for-the-badge&logo=apollo-graphql&logoColor=white"/> <img src="https://img.shields.io/badge/axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white"/> | 3.10.4 / 1.11.0 |
| **DICOM Viewer** | <img src="https://img.shields.io/badge/Cornerstone.js-000000?style=for-the-badge"/> | ^3.33.2 |
| **Package Manager**| <img src="https://img.shields.io/badge/npm-CB3837?style=for-the-badge&logo=npm&logoColor=white"/> | - |

<br>

#### Infrastructure & DevOps

| 구분 | 사용 기술 |
|:---:|:---|
| **Cloud Provider** | <img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white"/> |
| **Used Services** | <img src="https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=amazon-ec2&logoColor=white"/> <img src="https://img.shields.io/badge/RDS-527FFF?style=for-the-badge&logo=amazon-rds&logoColor=white"/> |
| **Database** | <img src="https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white"/> <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/> |
| **CI/CD** | <img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white"/> |
| **Containerization**| <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/> |
| **VCS & IDE** | <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white"/> <img src="https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white"/> <img src="https://img.shields.io/badge/VS%20Code-007ACC?style=for-the-badge&logo=visual-studio-code&logoColor=white"/> |

<br><br>
## 요구사항

<div style="text-align: center;">
    <img height="500px" src="./docs/Diagram/Usecase Diagram/유즈케이스%20다이어그램.png" alt="alt text" />
</div>

#### 요구사항 분석(기능 정리)
|요구사항|상세내용|
|:---:|---|
| 회원 관리 | 회원가입 & 로그인, 프로필 관리, 주문 내역 관리, 회원 탈퇴 |
| 상품 기능 | 상품 목록 조회, 상품 상세 페이지, 상품 검색 & 필터링, 상품 추천 |
| 장바구니 | 상품 담기 / 삭제, 수량 변경, 장바구니 관리, 총 금액 확인 |
| 주문 & 결제 | 주문서 작성, 결제 처리, 주문 내역 조회, 배송 관리 |
| 커뮤니티 & 지원 | 리뷰 작성/조회, 별점 평가, AI 챗봇 문의, 1:1 문의 |
| 판매자 기능 | 상품 관리, 판매  통계, 주문 관리, 고객 관리 |

<br><br>
## 구현 영상

## 구현 사진
### 메인화면

### 설정 


## 구현 사진 상세
<br>


<br><br>

## 소프트웨어 설계
<a href="./docs/Diagram/" target="_blank">소프트웨어 설계</a>
<br>

<br><br>
