package org.zerock.ex2.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired // 주입
    MemoRepository memoRepository;

    //insert 테스트
    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1,100).forEach(i -> { // IntStream을 사용해 1부터 100까지 생성
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    // select 테스트
    @Test
    public void testSelect(){
        //데이터베이스에 존재하는 mno
        Long mno  = 100L;

        Optional<Memo> result = memoRepository.findById(mno); // findById(mno) : 프라이머리키를 검색

        System.out.println("==================================");

        if(result.isPresent()){ // isPresent()라는 메서드를 통해, 만약(if) result에 값이 있으면
            Memo memo = result.get(); // result값을 get해서 memo에 담은다음
            System.out.println(memo); // result값이 담긴 memo를 출력(sysout)
        }
    }

    // update 테스트
    @Test
    public void testUpdate() {

        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        // mno.(100L).memoText("UpdateText") =>  100번째(100L)을 UpdateText로 바꿈

        System.out.println(memoRepository.save(memo));

    }


    // delete 테스트
    @Test
    public void testDelete() {

        Long mno = 100L;

        memoRepository.deleteById(mno);

    }

    @Test
    public void testPageDefault() { // 페이징 해주는 메서드

        //1페이지 10개
        Pageable pageable = PageRequest.of(0,10); // 선언 후,
        Page<Memo> result = memoRepository.findAll(pageable); // findAll : 전체 데이터를 읽어옴. (pageable)을 통해 페이징처리하면서 읽어옴

        System.out.println(result);
        System.out.println("---------------------------------------");
        System.out.println("Total Pages: "+result.getTotalPages()); // 전체 페이지 수
        System.out.println("Total Count: "+result.getTotalElements()); // 총 갯수
        System.out.println("Page Number: "+result.getNumber()); // 페이지 넘버
        System.out.println("Page Size: "+result.getSize()); // 사이즈(여기선 10)
        System.out.println("has next page?: "+result.hasNext()); // 다음 페이지가 있냐. ture, false로 출력됨
        System.out.println("first page?: "+result.isFirst()); // 이게 첫번째 페이지냐.  ture, false로 출력됨

    }


    @Test
    public void testSort() {

        Sort sort1 = Sort.by("mno").descending(); // descending =>  order by mno desc와 같은 의미
        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testQueryMethodWithPagable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L,50L, pageable); // findByMnoBetween(10L,50L, pageable) => where mno betwen 10 and 50와 같은 의미
        result.get().forEach(memo -> System.out.println(memo));
    }

    @Commit
    @Transactional
    @Test // 테스트코드에서 삭제처리할때는 @Commit, @Transactional 이 두개를 꼭 붙여줘야 함!!!
    public void testDeleteQueryMethods() {
        memoRepository.deleteMemoByMnoLessThan(10L);//deleteMemoByMnoLessThan(10L) => delete from memo where mno<10 와 같은 의미
    }
}
