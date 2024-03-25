package org.zerock.mreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //목록
    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno") Long mno){
        log.info("--------------list---------------");
        log.info("MNO: " + mno);

        List<ReviewDTO> reviewDTOList = reviewService.getListOfMovie(mno);

        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    //등록
    @PostMapping("/{mno}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO movieReviewDTO){
        log.info("--------------add MovieReview---------------");
        log.info("reviewDTO: " + movieReviewDTO);

        Long reviewnum = reviewService.register(movieReviewDTO);

        return new ResponseEntity<>( reviewnum, HttpStatus.OK);
    }

    //수정
    @PutMapping("/{mno}/{reviewnum}")
    @PreAuthorize("principal.username == #movieReviewDTO.mid") // 로그인한 ID와 board의 작성자가 같은지?
    public ResponseEntity<Long> modifyReview(@PathVariable Long reviewnum,
                                             @RequestBody ReviewDTO movieReviewDTO){
        log.info("---------------modify MovieReview--------------" + reviewnum);
        log.info("reviewDTO: " + movieReviewDTO);

        reviewService.modify(movieReviewDTO);

        return new ResponseEntity<>( reviewnum, HttpStatus.OK);
    }

    //삭제
    @DeleteMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> removeReview( @PathVariable Long reviewnum){
        log.info("---------------modify removeReview--------------");
        log.info("reviewnum: " + reviewnum);

        reviewService.remove(reviewnum);

        return new ResponseEntity<>( reviewnum, HttpStatus.OK);
    }

}


