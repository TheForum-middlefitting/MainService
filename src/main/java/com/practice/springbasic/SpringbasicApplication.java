package com.practice.springbasic;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@SpringBootApplication
public class SpringbasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbasicApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration()
				.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
				.setFieldMatchingEnabled(true)
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public MethodValidationPostProcessor getMethodValidationPostProcessor(){
//		MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
//		processor.setValidator(this.validator());
//		return processor;
//	}
//
//	@Bean
//	public LocalValidatorFactoryBean validator(){
//		return new LocalValidatorFactoryBean();
//	}

}
