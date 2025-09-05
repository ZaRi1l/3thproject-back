package com._thproject._thproject_web.postgresql.repository;

import com._thproject._thproject_web.postgresql.entity.ChatMessage;
import com._thproject._thproject_web.postgresql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 두 사용자 간의 모든 대화 내역을 시간 순으로 조회하는 쿼리 메소드
    List<ChatMessage> findBySenderAndRecipientOrRecipientAndSenderOrderByCreatedAtAsc(
            User user1, User user2, User user3, User user4
    );
}