package com.prodoblog.repository;

import com.prodoblog.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(int page);
}
