package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.entity.Memo;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    // save(), findById(), findAll(), deleteById() 메서들들을 기본적으로 사용가능


    // 아래 세 줄은 QueryMethod. 기본적으로 이 세줄을 작성하고 나서, test페이지로 넘어가서 테스트해봐야 함.
    // where mno between 10 and 20 order by mno desc
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // where mno between 10 and 20. 여기에 페이징 처리
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // delete from mno where mno<10
    void deleteMemoByMnoLessThan(Long num);
}
