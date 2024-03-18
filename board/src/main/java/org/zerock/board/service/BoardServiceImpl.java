package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository repository; // 자동주입
    private final ReplyRepository replyRepository; // 자동주입


    @Override
    public Long register(BoardDTO dto) {
        Board board=dtoToEntity(dto);
        repository.save(board);
        return board.getBno(); // insert 한 글번호를 리턴
    }

    @Override
    public BoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno); // repository에서 getBoardByBno(bno)를 받아와서

        Object[] arr = (Object[])result; // result를 Object[]배열로 변환해서 arr에 넣고...
        //......근데 왜 repository에서는 1건만 출력해서 배열을 안썻는데, 여기선 다시 배열에 저장하는거지?.
        // -> repository의 쿼리문을 보면, 받아올 파라미터가 board, member, bno 총 3개이기에, 이들을 각각 arr[0]~[2]까지 넣어서
        // 이 배열값을 BoardService의 entityToDTO를 이용해 변환시켜 리턴하는 형식임.

        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> board=repository.findById(boardDTO.getBno());
        if(board.isPresent()){ // Optional은 board!=null이 아닌, board.isPresene()를 사용
            Board b=board.get();
            b.changeTitle(boardDTO.getTitle());
            b.changeContent(boardDTO.getContent());

            repository.save(b);
        }
    }

    @Transactional // 댓글삭제->부모글 삭제 순으로 두 가지 모두 완료되어야 실행되게끔 구현하기 위해, Transactional 사용
    @Override
    public void removeWithReplies(Long bno) {
        //댓글 부터 삭제. 참조키가 걸려 있기 때문에, 댓글 먼저 지운 후 부모글을 지우는 방식으로 코딩해야함.
        replyRepository.deleteByBno(bno); // deleteByBno의 bno는 pk가 아닌 참조키로써, repository를 통해 기본적으로 제공되는 메서드가 아니므로,
                                         // repository에서 메서드를 직접 만들어줘야 함.
        repository.deleteById(bno);     // deleteById의 bno는 primary key로써, repository를 만들면 기본적으로 제공되는 메서드

    }



    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);
        //jpql작성시, select 결과는 Object 타입에 저장
        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0],(Member)en[1],(Long)en[2]));

//        Page<Object[]> result = repository.getBoardWithReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending())  );
        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())  );



        return new PageResultDTO<>(result, fn);

    }
}
