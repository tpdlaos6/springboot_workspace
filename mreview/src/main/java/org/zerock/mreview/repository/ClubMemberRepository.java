package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.ClubMember;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {

    //회원정보 상세보기

    //EntityGraph를 써서 튜닝같은 걸 했다고 생각하면 됨.

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from ClubMember m where m.fromSocial = :social and m.email =:email")
    Optional<ClubMember> findByEmail(String email, boolean social);

}
