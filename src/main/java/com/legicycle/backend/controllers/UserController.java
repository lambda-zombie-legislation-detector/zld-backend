package com.legicycle.backend.controllers;

import com.legicycle.backend.exceptions.InvalidInputException;
import com.legicycle.backend.models.User;
import com.legicycle.backend.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController
{

    @Autowired
    private UserService userService;

    @ApiIgnore
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/users", produces = {"application/json"})
    public ResponseEntity<?> listAllUsers()
    {
        List<User> myUsers = userService.findAll();
        return new ResponseEntity<>(myUsers, HttpStatus.OK);
    }


    @ApiIgnore
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/user/{userId}", produces = {"application/json"})
    public ResponseEntity<?> getUser(@PathVariable Long userId)
    {
        User u = userService.findUserById(userId);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }


    @ApiOperation(value="Get the username of the current user", response = User.class)
    @GetMapping(value = "/getusername", produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserName(Authentication authentication)
    {
        return new ResponseEntity<>(authentication.getPrincipal(), HttpStatus.OK);
    }

    @ApiOperation(value="Adds a user with given username and password to the database", response = User.class)
    @PostMapping(value = "/register", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewUser(
            @ApiParam(value = "User to be added, JSON object with \"username\" and \"password\". \n\"userid\" is neither supported nor expected.", required = true)
            @Valid @RequestBody User newuser
    )
    {
        if (newuser.getPassword() == null || newuser.getUsername() == null) {
            throw new InvalidInputException("Must provide a valid username and password");
        }
        newuser =  userService.save(newuser);

        return new ResponseEntity<>(newuser, HttpStatus.CREATED);
    }


    @ApiOperation(value="Updates the given user, returns nothing")
    @PutMapping(value = "/user/{id}")
    public ResponseEntity<?> updateUser(
            @ApiParam(value = "User Object with updated fields", required = true) @RequestBody User updateUser,
            @ApiParam(value = "ID used to determine what user to update, comes from the URL path", required = true) @PathVariable long id)
    {
        userService.update(updateUser, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value="Deletes the given user, returns nothing")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUserById(
            @ApiParam(value = "ID used to determine what user to delete, comes from the URL path", required = true) @PathVariable long id)
    {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}