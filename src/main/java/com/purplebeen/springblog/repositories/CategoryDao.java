package com.purplebeen.springblog.repositories;

import com.purplebeen.springblog.beans.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryDao extends MongoRepository<Category,String> {
    public Category findCategoryByName(String categoryName);
}
