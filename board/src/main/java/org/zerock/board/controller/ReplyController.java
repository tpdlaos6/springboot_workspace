package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.Criteria;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.dto.ReplyPageDTO;
import org.zerock.board.service.ReplyService;

import java.util.List;

@RestController // ajax로 처리할 거기 때문에, @Controller가 아닌, @RestController 사용
                // 이전에 responseBody로 할 때는, @Controller 안에 있는 메서드에 구현
@RequestMapping("/replies/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService; //자동주입을 위해 final

    //목록
    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno ){
        log.info("bno: " + bno);
        return new ResponseEntity<>( replyService.getList(bno), HttpStatus.OK); // json 형태로 날림
    }

    //등록
    @PostMapping("")
    // 폼에 날라오는 데이터가 JSON형태로 넘어오는 데, 이걸 받아서 dto에 넣을 때 쓰는 에너테이션이, @RequestBody임!!!
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){
        Long rno = replyService.register(replyDTO);
        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    //삭제
    @DeleteMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {
        replyService.remove(rno);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    //수정
    @PutMapping("/{rno}")
    // 폼에 날라오는 데이터가 JSON형태로 넘어오는 데, 이걸 받아서 dto에 넣을 때 쓰는 에너테이션이, @RequestBody임!!!
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {
        replyService.modify (replyDTO );
        return new ResponseEntity<>("success ", HttpStatus.OK);
    }

    //페이징
    @GetMapping("/pages/{bno}/{page}")
    public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, @PathVariable("bno") Long bno){
        Criteria cri=new Criteria(page,10);
        return new ResponseEntity<>(replyService.getListPage(cri,bno),HttpStatus.OK);
    }

}
