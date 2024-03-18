package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.service.ReplyService;

import java.util.List;

@RestController // ajax로 처리할 거기 때문에, @Controller가 아닌, @RestController 사용
                // 이전에 responseBody로 할 때는, @Controller 안에 있는 메서드에 구현
@RequestMapping("/replies/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService; //자동주입을 위해 final
    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno ){
        log.info("bno: " + bno);
        return new ResponseEntity<>( replyService.getList(bno), HttpStatus.OK); // json 형태로 날림
    }
}