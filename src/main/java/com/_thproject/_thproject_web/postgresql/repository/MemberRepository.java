package com._thproject._thproject_web.postgresql.repository;

import com._thproject._thproject_web.postgresql.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 이 인터페이스가 데이터 접근 계층(Repository)임을 나타냅니다. (생략 가능)
public interface MemberRepository extends JpaRepository<Member, Long> {
    // JpaRepository<엔티티 클래스, 기본 키 타입>
}
