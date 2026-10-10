package example.day17_;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스프링 스케줄러 기능 활성화
public class AppStart {
    public static void main(String[] args) {
        SpringApplication.run( AppStart.class);
    }
}
 