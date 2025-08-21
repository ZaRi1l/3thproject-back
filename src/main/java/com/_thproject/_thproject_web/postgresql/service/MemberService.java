package com._thproject._thproject_web.postgresql.service;

import com._thproject._thproject_web.postgresql.dto.MemberDto;
import com._thproject._thproject_web.postgresql.entity.Member;
import com._thproject._thproject_web.postgresql.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 이 클래스가 비즈니스 로직을 담당하는 서비스 계층임을 나타냅니다.
@RequiredArgsConstructor // final이 붙은 필드를 초기화하는 생성자를 자동으로 만들어줍니다. (DI를 위함)
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 저장
    @Transactional // 이 메소드가 실행될 때 트랜잭션을 시작하고, 끝나면 커밋합니다. 오류 발생 시 롤백합니다.
    public Member saveMember(MemberDto.MemberRequestDto requestDto) {
        Member member = new Member(requestDto.getUsername(), requestDto.getEmail());
        return memberRepository.save(member);
    }

    // 모든 회원 조회
    @Transactional(readOnly = true) // 조회 기능이므로 readOnly=true 옵션을 주면 성능에 이점이 있습니다.
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }
}
