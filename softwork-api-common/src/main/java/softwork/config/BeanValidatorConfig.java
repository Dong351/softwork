package softwork.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class BeanValidatorConfig implements WebMvcConfigurer {
    @Autowired
    private MessageSource messageSource;

    /**
     * 实体类字段校验国际化引入
     */
    @Override
    public Validator getValidator() {
        return validator();
    }

    /**
     * 实体类字段校验国际化引入
     */
    @Bean
    public Validator validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }
}
