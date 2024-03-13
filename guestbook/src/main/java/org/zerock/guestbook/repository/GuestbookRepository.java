package org.zerock.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long>, QuerydslPredicateExecutor<Guestbook> {
// save(), findById, deleteById, findAll 등 기본 메서드 사용 가능
// QuerydslPredicateExecutor<Guestbook> => querydsl을 상속받았다는 의미. 제너릭으로 Entity명 써줘야 함.
}

