package example.day10;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor 
public class MemberService {
    private final MemberRepository memberRepository;
    // *** [*] 비크립트(단방향 암호화 사용) 라이브러리 객체주입
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // [1] 회원가입 = 등록 = Create = C 
    public boolean signup( MemberDto memberDto ){
        // 1) 회원가입/등록 할 정보들을 컨트롤러에게 받아
        // 2) entity 변환 
        MemberEntity memberEntity = memberDto.toEntity();
        // 3) entity save
            // *** [*] 저장 하기전에 평문(원본 비밀번호) --> 암호문 으로 변환 
            // passwordEncoder.encode("평문");
        String 암호문 = passwordEncoder.encode( memberDto.getMpwd() ); // 입력받은 비밀번호 평문->암호문
        memberEntity.setMpwd( 암호문 ); // 암호문을 엔티티에 대입
        MemberEntity savedEntity = memberRepository.save( memberEntity );
        // 4) confirm
        if( savedEntity.getMno() >= 1 ) return true;
        return false;
    }
}
