package org.zerock.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name="tbl_member") // 테이블명 변경하기
public class Member extends BaseEntity{

    @Id
    private String email;
    private String password;
    private String name;
}
