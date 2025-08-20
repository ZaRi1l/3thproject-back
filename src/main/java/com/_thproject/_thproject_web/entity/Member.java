package com._thproject._thproject_web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Member {
    @Id // 이 필드가 테이블의 Primary Key(기본 키)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 데이터베이스가 자동으로 생성(auto-increment)하도록 합니다.
    private Long id;

    @Column(nullable = false, unique = true) // 테이블의 컬럼과 매핑됩니다. nullable=false는 NOT NULL 제약조건, unique=true는 UNIQUE 제약조건을 의미합니다.
    private String username;

    @Column(nullable = false)
    private String email;

    // 생성자: username과 email을 받아서 객체를 생성합니다.
    public Member(String username, String email) {
        this.username = username;
        this.email = email;
    }
    
}
