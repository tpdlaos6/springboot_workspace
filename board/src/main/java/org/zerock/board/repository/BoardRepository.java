package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository  extends JpaRepository<Board,Long>, SearchBoardRepository {
    // save(), findById(), findAll(), deleteById() 기본적으로 사용 가능

    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    // select b.*, w.*
    // from board b left join tbl_member w
    // on b.writer_email=w.email
    // where b.bno=?
    Object getBoardWithWriter(@Param("bno") Long bno);


    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    // select b.*, r.*
    // from board b left join reply r
    // on b.bno=r.board_bno
    // where b.bno=?
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);


    //목록 보기
    @Query(value ="SELECT b, w, count(r) " +  // count(r)은 댓글갯수
            " FROM Board b " +
            " LEFT JOIN b.writer w " +
            " LEFT JOIN Reply r ON r.board = b " +
            " GROUP BY b",
            countQuery ="SELECT count(b) FROM Board b")
    // select b.*,w.*,count(r.rno)
    // from board b
    // left join tbl_member w
    // on b.writer_email=w.email
    // left join reply r
    // on r.board_bno=b.bno
    // group by b.bno
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);


    // 1건만. 즉, 상세보기
    @Query("SELECT b, w, count(r) " +
            " FROM Board b LEFT JOIN b.writer w " +
            " LEFT OUTER JOIN Reply r ON r.board = b" +
            " WHERE b.bno = :bno")
    // select b.*, w.*
    // from board b left join tbl_member w
    // on b.writer_email=w.email
    // left join reply r on r.board_bno=b.bno
    // where b.bno=?
    Object getBoardByBno(@Param("bno") Long bno);
}
