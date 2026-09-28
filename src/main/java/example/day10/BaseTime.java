package example.day10;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter // 2] 상속받은 엔티티가 멤버변수 사용
@MappedSuperclass // 1]엔티티의 상속용도 클래스
@EntityListeners( AuditingEntityListener.class )// 4] 해당 엔티티 자동 감사 적용
public class BaseTime {
    @CreatedDate // 3] 엔티티 생성 날짜/시간 주입
    private LocalDateTime createDate;
    @LastModifiedDate // 3] 엔티티 수정 날짜/시간 주입
    private LocalDateTime updateDate;
}