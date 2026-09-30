package example.day12;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MemberDto {
    private Long mno;
    private String mid;
    private String mpwd;
    private String mname;
    private String role; // 권한 (user / admin)
    // + BaseTime 멤버변수
    private String createDate;
    private String updateDate;
    // + DTO --> ENTITY : 주로 회원가입(저장) 또는 수정 시 사용
    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .mid(this.mid)
                .mpwd(this.mpwd)
                .mname(this.mname)
                .role(this.role != null && !this.role.isBlank() ? this.role : "user")
                .build();
    }
    // + ENTITY --> DTO : 주로 조회/반환(내 정보 조회 등) 시 사용 (비밀번호 제외)
    public static MemberDto from(MemberEntity entity) {
        return MemberDto.builder()
                .mno(entity.getMno())
                .mid(entity.getMid()) // 보안상 비밀번호(mpwd)는 반환 DTO에서 제외
                .mname(entity.getMname())
                .role(entity.getRole())
                .createDate(entity.getCreateDate().toString())
                .updateDate(entity.getUpdateDate().toString())
                .build();
    }
}