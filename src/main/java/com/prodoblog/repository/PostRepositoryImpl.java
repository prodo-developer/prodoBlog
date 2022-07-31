package com.prodoblog.repository;

import com.prodoblog.domain.Post;
import com.prodoblog.domain.QPost;
import com.prodoblog.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

// 1. Bean객체를 불러오기위한 config가 필요함 (QueryDslConfig)
// 2. 쿼리를 만들기 위한 인터페이스 필요.
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
//                .offset((long) (postSearch.getPage() -1) * postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
