package org.zerock.ex2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_memo") // 테이블 이름을 바꾸고 싶을 때..
                        // 이게 업으면 클래스명인 Memo로 테이블이 만들어지고,
                        // 이걸 넣으면 name=안에 값인, tbl_memo로 테이블이 만들어진다.
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Memo {
    @Id // 프라이머리키로 설정하는 코드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increament로 설정해주는 에너테이션
    private Long mno; // 컬럼명은 mno로 하겠다는 의미

    @Column(length = 200, nullable = false) // varchar(200) not null 로 만들겠다는 의미
    private String memoText;
}
