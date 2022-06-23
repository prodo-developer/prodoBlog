package com.prodoblog.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreate {

    public String title;
    public String content;

    @Override
    public String toString() {
        return "PostCreate{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
