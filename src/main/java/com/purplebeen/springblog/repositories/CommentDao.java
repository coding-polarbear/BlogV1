package com.purplebeen.springblog.repositories;

import com.purplebeen.springblog.beans.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentDao extends MongoRepository<Comment,String> {
}
