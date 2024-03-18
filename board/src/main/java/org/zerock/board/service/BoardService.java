package org.zerock.board.service;

import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

public interface BoardService {

    //등록
    Long register(BoardDTO dto);

    //상세보기
    BoardDTO get(Long bno);

    //수정
    void modify(BoardDTO boardDTO);

    //삭제(댓글과 함께)
    void removeWithReplies(Long bno);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
    default Board dtoToEntity(BoardDTO dto){

        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }

    //BoardDTO 안에 있는 wirter는 Member테이블의 참조값이므로, entityToDTO의 파라미터 안에 Member member를 추가.
    // 또한 마찬가지 이유로 replyCount도...????????????
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) //int로 처리하도록
                .build();

        return boardDTO;

    }


}
