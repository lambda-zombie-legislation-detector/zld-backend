package com.legicycle.backend;

import com.legicycle.backend.daos.RoleDao;
import com.legicycle.backend.daos.UserDao;
import com.legicycle.backend.models.Role;
import com.legicycle.backend.models.User;
import com.legicycle.backend.models.UserRoles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@Transactional
@Component
public class SeedData implements CommandLineRunner
{
    RoleDao rolerepos;
    UserDao userrepos;

    public SeedData(RoleDao rolerepos, UserDao userrepos)
    {
        this.rolerepos = rolerepos;
        this.userrepos = userrepos;
    }

    @Override
    public void run(String[] args) throws Exception
    {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        r1 = rolerepos.save(r1);
        r2 = rolerepos.save(r2);

        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(), r1));
        admins.add(new UserRoles(new User(), r2));

        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(), r2));

        User u1 = new User("user", "password", users);
        User u2 = new User("admin", "password", admins);

        ArrayList<String> sampleSearches =
                new ArrayList<>(Arrays.asList("Macro Economic Policy", "Healthcare", "Gun Reform"));
        u1.setSearches(sampleSearches);

        userrepos.save(u1);
        userrepos.save(u2);
    }
}
