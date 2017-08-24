package com.purplebeen.springblog.repositories;

import com.purplebeen.springblog.beans.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDao extends MongoRepository<User,String> {
}
