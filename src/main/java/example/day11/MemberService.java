package example.day11;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 1. 회원가입
    @Transactional
    public boolean signup(MemberDto memberDto) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberDto.getMpwd());
        memberDto.setMpwd(encodedPassword);
        // 엔티티 변환 후 DB 저장
        MemberEntity savedEntity = memberRepository.save(memberDto.toEntity());
        return savedEntity.getMno() >= 1;
    }

    // 2. 로그인 검증
    public MemberDto login(MemberDto memberDto) {
        // 아이디로 엔티티 조회 (Optional 확인)
        Optional<MemberEntity> optionalMember = memberRepository.findByMid(memberDto.getMid());
        // 1) 아이디가 존재하지 않는 경우
        if (!optionalMember.isPresent()) {
            return null;
        }
        MemberEntity memberEntity = optionalMember.get();
        // 2) 비밀번호가 일치하지 않는 경우
        boolean isMatch = passwordEncoder.matches(memberDto.getMpwd(), memberEntity.getMpwd());
        if (!isMatch) {
            return null;
        }
        // 3) 인증 성공: 보안상 비밀번호를 제외한 DTO 반환
        return MemberDto.from(memberEntity);
    }

    // 3. 내 정보 조회 (회원번호 기반)
    public MemberDto getMyInfo(Long mno) {
        Optional<MemberEntity> optionalMember = memberRepository.findById(mno);
        if (!optionalMember.isPresent()) {
            return null;
        }
        return MemberDto.from(optionalMember.get());
    }
}