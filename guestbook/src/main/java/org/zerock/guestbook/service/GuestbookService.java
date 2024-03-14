package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookService {
    //등록
    Long register(GuestbookDTO dto);

    //목록
    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    //상세보기
    GuestbookDTO read(Long gno);

    //수정
    void modify(GuestbookDTO dto);

    //삭제
    void remove(Long gno);

    default Guestbook dtoToEntity(GuestbookDTO dto) { // DTO를 Entity로 변환
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default GuestbookDTO entityToDto(Guestbook entity){ // Entity를 DTO로 변환
        GuestbookDTO dto  = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;

    }
}
