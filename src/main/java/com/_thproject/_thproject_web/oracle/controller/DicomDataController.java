package com._thproject._thproject_web.oracle.controller;

import com._thproject._thproject_web.oracle.dto.StudyDto;
import com._thproject._thproject_web.oracle.repository.StudyRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class DicomDataController {

    private final StudyRepository studyRepository;

    // 생성자를 통해 StudyRepository를 주입받습니다.
    public DicomDataController(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    /**
     * schema.graphqls에 정의된 'study' Query를 처리하는 메서드입니다.
     * @QueryMapping 어노테이션이 이 메서드를 GraphQL 쿼리와 연결해줍니다.
     * @param studyKey 클라이언트가 쿼리에 담아 보낸 studyKey 값
     * @return 조회된 StudyDto 객체 (데이터가 없으면 null을 반환하며, 이는 GraphQL 표준)
     */
    @QueryMapping
    public StudyDto study(@Argument Long studyKey) {
        return studyRepository.findByStudyKey(studyKey).orElse(null);
    }
}