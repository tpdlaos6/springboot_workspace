package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
// extends QuerydslRepositorySupport : QueryDSL 사용
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board); // from board
        jpqlQuery.leftJoin(member).on(board.writer.eq(member)); // b left outer join tbl_member m on b.writer_email=m.email
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board)); // left outer join reply r on reply.board_bno=board.bno

        //SELECT b, w, count(r) FROM Board b
        //LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b

        // select board.*, member.*, count(r.rno)
        // from board b
        // left outer join tbl_member m on b.writer_email=m.email
        // left outer join reply r on reply.board_bno=board.bno
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder(); // where
        BooleanExpression expression = board.bno.gt(0L); // b.bno>0

        booleanBuilder.and(expression); // where b.bno>0

        if(type != null){
            String[] typeArr = type.split("");
            //검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t:typeArr) {
                switch (t){
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword)); // or b.title like '%1%'
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword)); // or m.email like '%1%'
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword)); // or b.content like '%1%'
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder); // where bno>0 and ( b.title like '%1%' or m.email like '%1%' or b.email like '%1%')
        }
        // select board.*, member.*, count(r.rno)
        // from board b
        // left outer join tbl_member m on b.writer_email=m.email
        // left outer join reply r on reply.board_bno=board.bno
        // where bno>0 and ( b.title like '%1%' or m.email like '%1%' or b.email like '%1%')
        tuple.where(booleanBuilder);

        //order by
        Sort sort = pageable.getSort();

        //tuple.orderBy(board.bno.desc());

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });

        tuple.groupBy(board);

        //page 처리.. ex) limit(0,10) : 첫번째 파라미터를 Offset, 두번째 파라미터를 Size라고 함
        //select board.*, member.*, count(r.rno)
        //from board b
        //left outer join tbl_member m on b.writer_email=m.email
        //left outer join reply r on reply.board_bno=board.bno
        //where bno>0 and (b.title like '%1%' or m.email like '%1%' or b.email like '%1%')
        //order by b.bno desc
        //group by b.bno
        //liimit 0,10
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch(); // 실행

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT: " +count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()), // result에서 하나 꺼내서, t에 넣고, Array(배열)로 변환. 이후 리스트로
                pageable,
                count);
    }
}

