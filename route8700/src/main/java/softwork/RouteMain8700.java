package softwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("softwork.mapper")
public class RouteMain8700 {
    public static void main(String[] args) {
        SpringApplication.run(RouteMain8700.class,args);
    }
}
