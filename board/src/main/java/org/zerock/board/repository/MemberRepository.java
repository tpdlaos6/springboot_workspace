package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.board.entity.Member;

public interface MemberRepository  extends JpaRepository<Member,String> { // member테이블은 email이 pk였기 때문에, String으로...
    // save(), findById(), findAll(), deleteById() 기본적으로 사용 가능
}
