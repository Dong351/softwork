package softwork.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import softwork.token.TokenMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenMethodArgumentResolver tokenMethodArgumentResolver;

    public WebMvcConfig(TokenMethodArgumentResolver tokenMethodArgumentResolver) {
        this.tokenMethodArgumentResolver = tokenMethodArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenMethodArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("threadPoolExecutor-");
        executor.initialize();
        configurer.setTaskExecutor(executor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 当访问/file下的资源时，会到/root/myFile/下去找
        // 例如访问：http://localhost:8080/file/1.png时会去找/root/myFile/1.png
        registry.addResourceHandler("/file/**").addResourceLocations("file:/root/usr/local/webapp/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}