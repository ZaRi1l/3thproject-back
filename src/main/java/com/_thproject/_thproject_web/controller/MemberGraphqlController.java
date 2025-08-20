package com._thproject._thproject_web.controller;

import com._thproject._thproject_web.dto.MemberDto;
import com._thproject._thproject_web.dto.MemberDto.MemberRequestDto;
import com._thproject._thproject_web.entity.Member;
import com._thproject._thproject_web.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller // @RestController가 아닌 그냥 @Controller를 사용합니다.
@RequiredArgsConstructor
public class MemberGraphqlController {

    private final MemberService memberService; // 기존에 만들었던 서비스를 그대로 재사용!

    // 스키마의 'Query' 타입에 정의된 'findAllMembers' 필드를 구현하는 메소드입니다.
    @QueryMapping
    public List<Member> findAllMembers() {
        return memberService.findAllMembers();
    }

    // 스키마의 'Mutation' 타입에 정의된 'createMember' 필드를 구현하는 메소드입니다.
    @MutationMapping
    public Member createMember(@Argument String username, @Argument String email) {
        // @Argument 어노테이션을 사용하여 스키마의 파라미터와 매핑합니다.
        MemberDto.MemberRequestDto requestDto = new MemberDto.MemberRequestDto(username, email); // DTO를 활용하는 경우
        return memberService.saveMember(requestDto);
    }
}