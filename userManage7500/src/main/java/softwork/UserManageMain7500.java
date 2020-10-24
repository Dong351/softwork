package softwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@MapperScan("softwork.mapper")
public class UserManageMain7500 {
    public static void main(String[] args) {
        SpringApplication.run(UserManageMain7500.class,args);
    }
}
