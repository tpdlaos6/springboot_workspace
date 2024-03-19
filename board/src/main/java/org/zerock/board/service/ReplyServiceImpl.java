package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.Criteria;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.dto.ReplyPageDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;
import org.zerock.board.repository.ReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service // 서비스impl은 꼭 붙여야
@RequiredArgsConstructor // 자동 주입을 위한
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyDTO replyDTO) { // dto를 받아서
        Reply reply = dtoToEntity(replyDTO); // entity로 변환 후
        replyRepository.save(reply);        // save
        return reply.getRno();
    }

    @Override
    public List<ReplyDTO> getList(Long bno) {
        List<Reply> result = replyRepository
                .getRepliesByBoardOrderByRno(Board.builder().bno(bno).build());
        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList()); //reply하나 꺼내서, entityToDTO로 변환한다음, toList에 저장
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO); // repository에서는 save()에 저장해야 하는 게 entity임
        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }

    @Override
    public ReplyPageDTO getListPage(Criteria cri, Long bno) {
        return new ReplyPageDTO(
                replyRepository.getCountByBno(bno),
                replyRepository.getListWithPaging(cri,bno)
        );
    }
}
