package com._thproject._thproject_web.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlConfig {

    /**
     * GraphQL 스키마에 정의된 'Long' 스칼라 타입에 대한 실제 구현체를 등록합니다.
     * 이 Bean이 등록되면, Spring for GraphQL은 'Long' 타입을 어떻게 처리해야 할지 알게 됩니다.
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.GraphQLLong);
    }
}