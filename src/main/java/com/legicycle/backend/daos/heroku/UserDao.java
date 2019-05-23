package com.legicycle.backend.daos.heroku;

import com.legicycle.backend.models.heroku.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long>
{
    User findByUsername(String username);
}
