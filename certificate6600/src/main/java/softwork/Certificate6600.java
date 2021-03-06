package softwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableCaching
@MapperScan("softwork.mapper")
public class Certificate6600 {
    public static void main(String[] args) {
        SpringApplication.run(Certificate6600.class,args);
    }
}
