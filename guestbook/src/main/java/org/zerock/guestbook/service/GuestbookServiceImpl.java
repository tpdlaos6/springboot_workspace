package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {
        Guestbook entity=dtoToEntity(dto);
        repository.save(entity);
        return entity.getGno();
    }

//    @Override
//    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
//        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
//        Page<Guestbook> result = repository.findAll(pageable);
//        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
//        return new PageResultDTO<>(result, fn );
//
//    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        BooleanBuilder booleanBuilder = getSearch(requestDTO); //검색 조건 처리
        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable); //Querydsl 사용
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn );
    }



    private BooleanBuilder getSearch(PageRequestDTO requestDTO){

        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = requestDTO.getKeyword();
        BooleanExpression expression = qGuestbook.gno.gt(0L); // gno > 0 조건만 생성
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0){ //검색 조건이 없는 경우
            return booleanBuilder;
        }

        //Querydsl 사용하여 동적 검색 조건을 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){ // contains()메서드를 사용. type이 't'가 포함되어 있으면(true) -> or title like '%키워드%'
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")){ // contains()메서드를 사용. type이 'c'가 포함되어 있으면(true) -> or content like '%키워드%'
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if(type.contains("w")){ // contains()메서드를 사용. type이 'w'가 포함되어 있으면(true) -> or writer like '%키워드%'
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
