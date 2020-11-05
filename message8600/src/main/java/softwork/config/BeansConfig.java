package softwork.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class BeansConfig {

    /**
     * 配置{@link Random}对象
     *
     * @return {@link Random} 实例
     */
    @Bean
    public Random random() {
        return new Random();
    }


}
