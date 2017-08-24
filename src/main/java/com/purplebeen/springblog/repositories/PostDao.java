package com.purplebeen.springblog.repositories;

import com.purplebeen.springblog.beans.Category;
import com.purplebeen.springblog.beans.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface PostDao extends MongoRepository<Post,String> {
    public Post findByTitle(String title);
    public List<Post> findByCategory(Category category);
}
