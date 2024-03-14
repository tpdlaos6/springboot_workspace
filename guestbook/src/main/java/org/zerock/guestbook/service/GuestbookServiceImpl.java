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

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {
        Guestbook entity=dtoToEntity(dto); // dto를 entity로 바꿔서
        repository.save(entity); // save()함수를 이용해 insert
                                // db테이블 만들때, gno에 auto_increament를 줬기에
        return entity.getGno(); // entity 내에 바뀐 gno번호를 리턴
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

    @Override
    public GuestbookDTO read(Long gno) {
        //findById의 리턴값이 Optional
        Optional<Guestbook> result=repository.findById(gno);
        return result.isPresent()?entityToDto(result.get()):null;
        // result가 값이 있으면(isPresent()), entity를 DTO로(entityToDto) 변환. 그렇지 않으면, 그대로(null)
    }

    @Override
    public void modify(GuestbookDTO dto) {
        //수정할 글 검색.
        // 본래는 dto를 entity로 받아와서 수정하는 정석적인 방법도 있음.
        // 그러나 여기에선 검색을 통해 title과 content만 찾아와서 바꾸는 방법으로 코딩.
        Optional<Guestbook> result=repository.findById(dto.getGno());
        if(result.isPresent()){
            Guestbook entity=result.get();
            entity.changeTitle(dto.getTitle()); // 제목 변경
            entity.changeContent(dto.getContent()); // 내용 변경
            // 날짜는 BaseEntity를 통해 자동으로 들어가도록 해놨기 때문에, 날짜를 변경하는 코드는 없음

            repository.save(entity); // save()는, 데이터가 없을 땐 insert로 동작. 데이터가 있을 땐 update로 동작
        }
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
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
