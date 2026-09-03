package example.day05;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
@MappedSuperclass // 현재 클래스는 상속용 매핑
@EntityListeners( AuditingEntityListener.class ) // 리스너 구현체 등록
public class BaseTime {
    @CreatedDate // 1. 레코드 생성시점
    private LocalDateTime createDate;
    @LastModifiedDate // 2. 레코드 변경시점
    private LocalDateTime updateDate;
}