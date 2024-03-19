package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    //coalesce => null 값일 때, 다른 값으로 바꿔주는 함수. 여기선 grade가 null이면 0점으로 보겠다는 뜻으로 코딩함
    @Query("select m, avg(coalesce(r.grade,0)),  count(r) from Movie m " +
        "left outer join Review r on r.movie = m group by m") // Movie와 Review를 left outer join함

    //테이블 하나에서 받는거면 그냥 Entity로 받으면 되는데, 함수나, 조인 등으로 받으면 Object로 받음
    Page<Object[]> getListPage(Pageable pageable);


//    @Query("select m, mi, avg(coalesce(r.grade,0)),  count(r) from Movie m " + // 여기서 mi는 MovieImage
//            "left outer join MovieImage mi on mi.movie = m " +
//            "left outer join Review  r on r.movie = m group by m ")
//    Page<Object[]> getListPage(Pageable pageable);


    //상세보기
    @Query("select m, mi ,avg(coalesce(r.grade,0)),  count(r)" + // 여기서 mi는 MovieImage
            " from Movie m left outer join MovieImage mi on mi.movie = m " +
            " left outer join Review  r on r.movie = m "+
            " where m.mno = :mno group by mi")
    List<Object[]> getMovieWithAll(Long mno);


}
