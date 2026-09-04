package example.day05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA Entity 등록/수정 감시 기능 활성화
public class AppStart {
    public static void main(String[] args) {
        SpringApplication.run( AppStart.class );
    }
}
