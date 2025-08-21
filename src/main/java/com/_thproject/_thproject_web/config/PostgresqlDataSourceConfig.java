// src/main/java/com/_thproject/_thproject_web/config/PostgresqlDataSourceConfig.java

package com._thproject._thproject_web.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties; // 변경된 import
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com._thproject._thproject_web.postgresql.repository",
        entityManagerFactoryRef = "postgresqlEntityManagerFactory",
        transactionManagerRef = "postgresqlTransactionManager"
)
public class PostgresqlDataSourceConfig {

    // 1단계: PostgreSQL 프로퍼티를 읽어오는 전용 Bean 생성
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.postgresql")
    public DataSourceProperties postgresqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    // 2단계: 위에서 만든 프로퍼티 객체를 사용하여 DataSource Bean 생성
    @Primary
    @Bean(name = "postgresqlDataSource")
    public DataSource postgresqlDataSource() {
        return postgresqlDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "postgresqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgresqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        return builder
                .dataSource(postgresqlDataSource())
                .packages("com._thproject._thproject_web.postgresql.entity")
                .persistenceUnit("postgresql")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "postgresqlTransactionManager")
    public PlatformTransactionManager postgresqlTransactionManager(
            final LocalContainerEntityManagerFactoryBean postgresqlEntityManagerFactory) {
        return new JpaTransactionManager(postgresqlEntityManagerFactory.getObject());
    }
}