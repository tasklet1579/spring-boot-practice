package edu.self.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@SpringBootApplication
public class Application {
    /*
     * In spring if we register LocalValidatorFactoryBean to bootstrap
     * javax.validation.ValidatorFactory ConstraintValidator classes are
     * loaded as Spring Bean. That means we can have benefit of Spring's
     * dependency injection in validator classes.
     */
    @Bean
    public Validator defaultValidator() {
        return new LocalValidatorFactoryBean();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
