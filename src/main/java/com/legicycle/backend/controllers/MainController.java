package com.legicycle.backend.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class MainController {

    @ApiOperation(value="get the list of searchable fields")
    @GetMapping(value = "/search", produces="application/json")
    public ResponseEntity<?> getSearchFields()
    {
        // If we're using this it will return a list of fields that we are able to search
        return new ResponseEntity<>("If you see this it worked", HttpStatus.OK);
    }

    @PostMapping(value = "/search", consumes = "application/json", produces="application/json")
    public ResponseEntity<?> searchByCriteria(@Valid @RequestBody HashMap<String, String> data)
    {
        String searchTerm;
        if (data.containsKey("searchTerm")) {
            searchTerm = data.get("searchTerm");
        }
        // This should return a list of legislation based on the search criteria
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
