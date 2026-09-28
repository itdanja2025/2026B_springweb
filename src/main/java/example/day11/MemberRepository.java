package example.day11;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 로그인 시 아이디로 회원 조회용
    Optional<MemberEntity> findByMid(String mid);
}