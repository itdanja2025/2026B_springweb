package example.day15_;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_log")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryLog {

    @Id
    @Column(nullable = false, length = 30)
    private String orderId;          // 주문번호 (예: ORD-202610-001)

    @Column(nullable = false, length = 30)
    private String courier;          // 택배사 (CJ대한통운, 한진택배, 로젠택배 등)

    @Column(nullable = false, length = 30)
    private String originHub;        // 물류 허브 (옥천HUB, 군포HUB, 용인HUB 등)

    @Column(nullable = false)
    private Integer deliveryDays;    // 배송 소요 일수

    @Column(nullable = false)
    private Boolean isReturned;      // 반품 여부 (true/false)

    @Column(length = 255)
    private String returnReason;     // 반품 사유 / 고객 메모

    @Column(nullable = false)
    private String logDate;       // 발생 일자
}