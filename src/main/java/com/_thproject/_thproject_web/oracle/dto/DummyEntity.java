package com._thproject._thproject_web.oracle.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * 이 클래스는 실제 DB 테이블과 매핑되지 않습니다.
 * Spring Data JPA의 JpaRepository<T, ID> 인터페이스를 사용하기 위해
 * 형식적으로 T 자리에 넣어주기 위한 껍데기(placeholder) 엔티티입니다.
 */
@Entity
public class DummyEntity {
    @Id
    private Long id;
}