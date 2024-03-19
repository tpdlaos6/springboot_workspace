package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.dto.Criteria;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    // save(), findById(), findAll(), deleteById() 기본적으로 사용 가능

    @Modifying
    @Query("delete from Reply r where r.board.bno=:bno") // native쿼리가 아니기 때문에, 대소문자 구분을 해줘야 함!!!!!!!
        // delete from Reply r where r.board.bno=:bno => Reply테이블에서 board의 bno가 특정값(:bno)과 일치하는 모든 레코드를 삭제
    void deleteByBno(Long bno);

    List<Reply> getRepliesByBoardOrderByRno(Board board);
    // getRepliesByBoardOrderByRno => select * from reply where bno=? order by rno

    //목록 with paging. parameter가 2개 이상일 때 @Param사용 필요.
    public List<ReplyDTO> getListWithPaging(@Param("cri") Criteria cri, @Param("bno") Long bno);

    //댓글 개수
    public int getCountByBno(Long bno);
}
