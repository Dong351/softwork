package softwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableAsync
@EnableDiscoveryClient
@MapperScan("softwork.mapper")
public class TeamMain8500 {
    public static void main(String[] args) {
        SpringApplication.run(TeamMain8500.class,args);
    }
}
