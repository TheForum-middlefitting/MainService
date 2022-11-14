package com.practice.springbasic.config;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Properties;

//@Configuration
@EnableWebMvc
public class Config {
//    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor mvProcessor = new MethodValidationPostProcessor();
        mvProcessor.setProxyTargetClass(true);
        return mvProcessor;
//        return new MethodValidationPostProcessor();
    }

//    @Bean
//    public LocalValidatorFactoryBean validatorAllowingParallelMethodParameterConstraints()
//    {
//        LocalValidatorFactoryBean defaultValidator = ValidationAutoConfiguration.defaultValidator();
//
//        Properties properties = new Properties();
//        properties.put( HibernateValidatorConfiguration.ALLOW_PARALLEL_METHODS_DEFINE_PARAMETER_CONSTRAINTS, "true" );
//        defaultValidator.setValidationProperties( properties );
//
//        return defaultValidator;
//    }
}