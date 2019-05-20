package com.legicycle.backend.daos;

import com.legicycle.backend.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long>
{
    User findByUsername(String username);
}
