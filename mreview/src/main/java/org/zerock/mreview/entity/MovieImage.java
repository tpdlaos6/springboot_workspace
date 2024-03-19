package org.zerock.mreview.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "movie") //연관 관계시 항상 주의. ManyToOne 걸려있는 항목은 항상 @ToString으로 빼줘야 함
public class MovieImage  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;
    private String uuid; // universal unique id. 절대 중복되지 않는 값
    private String imgName;
    private String path; // 2024/03/19 형식

    @ManyToOne(fetch = FetchType.LAZY) //무조건 lazy로
    private Movie movie;


}
