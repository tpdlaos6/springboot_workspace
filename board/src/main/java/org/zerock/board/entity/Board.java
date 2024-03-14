package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Entity
@EnableJpaAuditing
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer") // writer가 member의 참조타입인데.. board를 가지고 toString 오버라이드 할 때, 'writer'는 빼고 오버라이드 하라는 소리.
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long bno;

    private String title;

    private String content;

    // @ManoToOne의 두 가지 옵션
    //Eaget : 즉시 로딩(바로 보여줄 거냐)
    //Laze : 지연 로딩(나중에 보여줄 거냐)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }


}
