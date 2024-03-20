package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    // review를 갖고 오기 위해, member 데이터만 갖고 오면 됨. 그러나 현재 Review엔터티를 보면 ManyToOne이 movie에도 걸려있고, member에도 걸려있음.
    // 사실 native쿼리로 짜면 리뷰테이블에서 무비 번호를 조회하면 되는 간단한 일이지만,
    // jpa는 모든 데이터를 다 갖고 오는 게 디폴트이므로, 필요 없는 회원의 모든 정보들까지 다 땡겨옴.
    // 이걸 걸러내기 위한 의도로써, 필요없는 member데이터를 걸러내고자 EntityGraph 코드를 이용함...

    @EntityGraph(attributePaths = {"member"}, type= EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);


    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(Member member);
}
