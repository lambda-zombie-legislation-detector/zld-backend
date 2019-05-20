package com.legicycle.backend.services;

import com.legicycle.backend.exceptions.ResourceNotFoundException;
import com.legicycle.backend.models.Role;
import com.legicycle.backend.models.UserRoles;
import com.legicycle.backend.daos.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "roleService")
public class RoleServiceImpl implements RoleService
{
    @Autowired
    RoleDao roledao;

    @Override
    public ArrayList<Role> findAll()
    {
        ArrayList<Role> list = new ArrayList<>();
        roledao.findAll().iterator().forEachRemaining(list::add);
        return list;
    }


    @Override
    public Role findRoleById(long id)
    {
        return roledao.findById(id).orElseThrow(() -> new ResourceNotFoundException(Long.toString(id)));
    }


    @Override
    public void delete(long id)
    {
        roledao.findById(id).orElseThrow(() -> new ResourceNotFoundException(Long.toString(id)));
        roledao.deleteById(id);
    }


    @Transactional
    @Override
    public Role save(Role role)
    {
        return roledao.save(role);
    }

    @Transactional
    @Override
    public Role update(Role role, long id)
    {
        Role currentRole = roledao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Long.toString(id)));

        if (role.getName() != null)
        {
            currentRole.setName((role.getName()));
        }

        if (role.getUserRoles().size() > 0)
        {
            // with so many relationships happening, I decided to go
            // with old school queries
            // delete the old ones
            roledao.deleteUserRolesByRoleId(currentRole.getRoleid());

            // add the new ones
            for (UserRoles ur : role.getUserRoles())
            {
                roledao.insertUserRoles(ur.getUser().getUserid(), id);
            }
        }

        return roledao.save(currentRole);
    }

    @Transactional
    @Override
    public void saveUserRole(long userid, long roleid)
    {
        roledao.insertUserRoles(userid, roleid);
    }

    @Transactional
    @Override
    public void deleteUserRole(long userid, long roleid)
    {
        roledao.deleteUserRoles(userid, roleid);
    }

}
