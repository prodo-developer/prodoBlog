package com.prodoblog.repository;

import com.prodoblog.domain.Post;
import com.prodoblog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
