package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mreview.entity.Member;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .email("r"+i +"@zerock.org")
                    .pw("1111") // 더미이기 때문에 일단 1111로.. 나중에 암호화처리 할 예정.
                    .nickname("reviewer"+i).build();
            memberRepository.save(member);
        });
    }

//    @Commit
//    @Transactional
//    @Test
//    public void testDeleteMember() {
//
//        Long mid = 1L; //Member의 mid
//
//        Member member = Member.builder().mid(mid).build();
//
//        //기존
//        //memberRepository.deleteById(mid);
//        //reviewRepository.deleteByMember(member);
//
//        //순서 주의
//        reviewRepository.deleteByMember(member);
//        memberRepository.deleteById(mid);
//    }


}
