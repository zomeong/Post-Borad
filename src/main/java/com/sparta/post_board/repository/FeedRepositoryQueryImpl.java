package com.sparta.post_board.repository;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.post_board.entity.Feed;
import com.sparta.post_board.entity.QFeed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class FeedRepositoryQueryImpl implements FeedRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Feed> search(String keyword, Pageable pageable){
        var query = jpaQueryFactory.select(QFeed.feed)
                .from(QFeed.feed)
                .where(
                        QFeed.feed.title.like("%" + keyword + "%")
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        var feeds = query.fetch();
        long totalSize = countQuery(keyword).fetch().get(0);

        return PageableExecutionUtils.getPage(feeds, pageable, () -> totalSize);
    }

    private JPAQuery<Long> countQuery(String keyword) {
        return jpaQueryFactory.select(Wildcard.count)
                .from(QFeed.feed)
                .where(
                        QFeed.feed.title.like("%" + keyword + "%")
                );
    }
}
