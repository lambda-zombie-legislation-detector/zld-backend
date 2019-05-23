package com.legicycle.backend.daos.heroku;

import com.legicycle.backend.models.heroku.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RoleDao extends CrudRepository<Role, Long>
{
    @Transactional
    @Modifying
    @Query(value = "DELETE from UserRoles where userid = :userid")
    void deleteUserRolesByUserId(long userid);

    @Transactional
    @Modifying
    @Query(value = "DELETE from UserRoles where roleid = :roleid")
    void deleteUserRolesByRoleId(long roleid);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO UserRoles(userid, roleid) values (:userid, :roleid)", nativeQuery = true)
    void insertUserRoles(long userid, long roleid);

    @Transactional
    @Modifying
    @Query(value = "DELETE from UserRoles where userid = :userid AND roleid = :roleid", nativeQuery = true)
    void deleteUserRoles(long userid, long roleid);

    Role findRoleByName(String name);
}
