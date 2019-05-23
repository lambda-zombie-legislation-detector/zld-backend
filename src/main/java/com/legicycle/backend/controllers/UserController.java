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
import java.util.ArrayList;
import java.util.HashMap;
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


    @ApiOperation(value="Get the username of the current user", response = String.class)
    @GetMapping(value = "/getusername", produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserName(Authentication authentication)
    {
        return new ResponseEntity<>(authentication.getName(), HttpStatus.OK);
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
        System.out.println(newuser);
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

    //get users searches
    @ApiOperation(value="Returns an Array of Search terms saved by the user", response=String.class, responseContainer="List")
    @GetMapping("/user/searches")
    public ResponseEntity<?> getUserSearches(Authentication authentication) {
        User u = userService.findUserByUsername(authentication.getName());
        ArrayList<String> searches = new ArrayList<>();
        u.getSearches().iterator().forEachRemaining(searches::add);
        return new ResponseEntity<>(searches, HttpStatus.OK);
    }

    //add search item to current user
    @ApiOperation(value="Adds given search term to active user, returns Active users info with updated data", response=User.class)
    @PutMapping("/user/searches")
    public ResponseEntity<?> addToUserSearches(Authentication authentication,
            @ApiParam(value="Json object with a key \"search\" containing the search term you would like to save") @Valid @RequestBody HashMap<String, String> hashFromJson) {

        String search;
        if (hashFromJson.containsKey("search")) {
            search = hashFromJson.get("search");
        } else throw new InvalidInputException("could not find a valid entry for \"search\"");

        User u = userService.findUserByUsername(authentication.getName());
        u = userService.saveSearch(u, search);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    //delete search item
    @ApiOperation(value="Deletes given search term from active user, returns Active users info with updated data", response=User.class)
    @DeleteMapping("/user/searches")
    public ResponseEntity<?> removeUserSearch(Authentication authentication,
            @ApiParam(value="Json object with a key \"search\" containing the search term you would like to delete") @Valid @RequestBody HashMap<String, String> hashFromJson) {
        String search;
        if (hashFromJson.containsKey("search")) {
            search = hashFromJson.get("search");
        } else throw new InvalidInputException("could not find a valid entry for \"search\"");
        User u = userService.findUserByUsername(authentication.getName());

        u = userService.removeSearch(u, search);

        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @DeleteMapping("/user/allsearches")
    public ResponseEntity<?> removeAllUserSearches(Authentication authentication) {
        User u = userService.findUserByUsername(authentication.getName());
        return new ResponseEntity<>(userService.removeAllSearches(u), HttpStatus.OK);
    }

}