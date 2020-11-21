package softwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("softwork.mapper")
public class ChatMain5000 {
    public static void main(String[] args) {
        SpringApplication.run(ChatMain5000.class,args);
    }
}
