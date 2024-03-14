package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.board.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    // save(), findById(), findAll(), deleteById() 기본적으로 사용 가능
}
