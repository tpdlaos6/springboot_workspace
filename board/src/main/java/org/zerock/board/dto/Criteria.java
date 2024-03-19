package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class Criteria {
    private int pageNum; // 페이지 번호
    private int amount; // 한 페이지에 출력되는 글 수

    private String type; // 검색컬럼. title, content, writer
    private String keyword; // 검색어

    public Criteria() {
        this(1, 10);
    }

    public int getSkip() {
        return (this.pageNum-1)*this.amount;
    }

    public Criteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
    }
}