package org.zerock.mreview.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Set;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity {

    @Id
    private String email; // 이게 ID. 즉, 스프링에서는 USERNAME

    private String password;

    private String name;

    private boolean fromSocial; // 소셜로그인인지 아닌지 구분

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<ClubMemberRole> roleSet; // OneToMany 처럼 하나가 많은쪽을 바라보게 될 시, 레퍼런스가 여러개가 있을 수 있기에
                                        // 보통 list를 사용하지만, 여기선 중복 방지를 위해 Set<>을 사용함
                                        // ManyToOne을 걸때는 많은쪽에서 하나를 바라보기 때문에, 당연히 레퍼런스가 하나

    public void addMemberRole(ClubMemberRole clubMemberRole){
        roleSet.add(clubMemberRole);
    }

}
