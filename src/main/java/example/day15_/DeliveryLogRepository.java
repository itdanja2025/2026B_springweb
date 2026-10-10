package example.day15_;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryLogRepository extends JpaRepository<DeliveryLog, String> {
}