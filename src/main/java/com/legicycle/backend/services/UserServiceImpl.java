package com.legicycle.backend.services;

import com.legicycle.backend.daos.RoleDao;
import com.legicycle.backend.daos.UserDao;
import com.legicycle.backend.exceptions.ResourceNotFoundException;
import com.legicycle.backend.models.Role;
import com.legicycle.backend.models.User;
import com.legicycle.backend.models.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService
{

    @Autowired
    private UserDao userdao;

    @Autowired
    private RoleDao roledao;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws ResourceNotFoundException
    {
        User user = userdao.findByUsername(username);
        if (user == null)
        {
            throw new ResourceNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthority());
    }

    public User findUserById(long id) throws ResourceNotFoundException
    {
        return userdao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Long.toString(id)));
    }

    public List<User> findAll()
    {
        List<User> list = new ArrayList<>();
        userdao.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public void delete(long id)
    {
        if (userdao.findById(id).isPresent())
        {
            userdao.deleteById(id);
        }
        else
        {
            throw new ResourceNotFoundException(Long.toString(id));
        }
    }

    @Transactional
    @Override
    public User save(User user)
    {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPasswordNoEncrypt(user.getPassword());

        ArrayList<UserRoles> newRoles = new ArrayList<>();
        Role r = roledao.findRoleByName("user");

        newRoles.add(new UserRoles(newUser, r));
        newUser.setUserRoles(newRoles);

        return userdao.save(newUser);
    }

    @Transactional
    @Override
    public User update(User user, long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userdao.findByUsername(authentication.getName());

        if (currentUser != null)
        {
            if (id == currentUser.getUserid())
            {
                if (user.getUsername() != null)
                {
                    currentUser.setUsername(user.getUsername());
                }

                if (user.getPassword() != null)
                {
                    currentUser.setPasswordNoEncrypt(user.getPassword());
                }

                if (user.getUserRoles().size() > 0)
                {
                    // with so many relationships happening, I decided to go
                    // with old school queries
                    // delete the old ones
                    roledao.deleteUserRolesByUserId(currentUser.getUserid());

                    // add the new ones
                    for (UserRoles ur : user.getUserRoles())
                    {
                        roledao.insertUserRoles(id, ur.getRole().getRoleid());
                    }
                }
                return userdao.save(currentUser);
            }
            else
            {
                throw new ResourceNotFoundException(Long.toString(id) + " Not current user");
            }
        }
        else
        {
            throw new ResourceNotFoundException(authentication.getName());
        }

    }
}
