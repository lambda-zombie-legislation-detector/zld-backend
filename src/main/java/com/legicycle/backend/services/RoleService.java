package com.legicycle.backend.services;

import com.legicycle.backend.models.heroku.Role;

import java.util.ArrayList;

public interface RoleService
{
    ArrayList<Role> findAll();

    Role findRoleById(long id);

    void delete(long id);

    Role update(Role role, long id);

    Role save(Role role);

    void saveUserRole(long userid, long roleid);

    void deleteUserRole(long userid, long roleid);
}