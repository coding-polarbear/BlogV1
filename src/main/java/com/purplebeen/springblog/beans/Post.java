package com.purplebeen.springblog.beans;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private String id;
    private String title;
    private String url;
    private Category category;
    private String content;
    private String imageUrl;
    private Date regDate;
    private String aurthor;
    private List<Comment> commentList = new ArrayList<>();
}
