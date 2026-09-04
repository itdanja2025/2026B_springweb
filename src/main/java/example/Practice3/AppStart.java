package example.Practice3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA Auditing(트리거) 활성화
public class AppStart {
    public static void main(String[] args) {
        SpringApplication.run( AppStart.class);
    }
}

