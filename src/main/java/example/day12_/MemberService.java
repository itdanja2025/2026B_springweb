package example.day12_;

import java.util.Optional;

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
    // [2] 로그인 = 조회 = Read = R 
    public MemberDto login( MemberDto memberDto ){
        // 1) 컨트롤러에게 로그인시 입력받은 아이디/비밀번호 받는다.
        // 2) 입력받은 아이디가 존재하는지 검증 , findByMid 추상정의함.
        MemberEntity memberEntity = memberRepository.findByMid( memberDto.getMid() );
        if( memberEntity == null ) return null; // [로그인실패] 아이디가 존재하지 않으면 null 반환
        // 3) 존재하면 **** 평문(로그인시입력받은비밀번호) 과 암호문(회원가입시입력받은비밀번호) 비교!!! ****
        // passwordEncoder.matches("평문", "암호문");
        boolean 비밀번호일치 = passwordEncoder.matches(memberDto.getMpwd(), memberEntity.getMpwd() );
        if( 비밀번호일치 == false ) return null; // [로그인실패] 비밀번호불일치
        // 4) entity --> dto 변환하여 반환) , 로그인 전용 loginDto 있으면 더 좋다.
        return MemberDto.from(memberEntity);
    }

    // [3] 내 정보 조회(PK:회원번호)
    public MemberDto getMyInfo( Long mno ){
        // 1) 컨트롤러에게 조회할 회원번호 받는다.
        // 2) findById 이용하여 회원번호 조회
        Optional<MemberEntity> optional = memberRepository.findById(mno);
        if( optional.isPresent() ){ // 3)조회 결과 존재하면
            MemberEntity memberEntity = optional.get(); // 엔티티 꺼내서
            return MemberDto.from(memberEntity); // dto 변환하여 반환 
        }
        return null; // 4) 조회 결과 없으면 null 반환
    }

}
