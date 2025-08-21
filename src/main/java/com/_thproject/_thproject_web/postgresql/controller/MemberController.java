package com._thproject._thproject_web.postgresql.controller;

import com._thproject._thproject_web.postgresql.dto.MemberDto;
import com._thproject._thproject_web.postgresql.entity.Member;
import com._thproject._thproject_web.postgresql.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "members", description = "회원 관리 API") // API 그룹에 대한 태그 추가
@RestController // 이 클래스가 RESTful API 컨트롤러임을 나타냅니다. (@Controller + @ResponseBody)
@RequestMapping("/api/members") // 이 컨트롤러의 모든 메소드는 /api/members 라는 공통 URL 경로를 가집니다.
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 생성 API
    @Operation(summary = "회원 생성", description = "새로운 회원을 생성합니다.") // 각 API에 대한 설명 추가
    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody MemberDto.MemberRequestDto requestDto) {
        Member savedMember = memberService.saveMember(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    // 모든 회원 조회 API   
    @Operation(summary = "모든 회원 조회", description = "모든 회원을 조회합니다.") // 각 API에 대한 설명 추가
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        return ResponseEntity.ok(members);
    }
}