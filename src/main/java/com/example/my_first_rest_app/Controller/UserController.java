package com.example.my_first_rest_app.Controller;

import com.example.my_first_rest_app.Model.User;
import com.example.my_first_rest_app.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepo;

    //Creates a new user
    @PostMapping("/registerUser")
    public ResponseEntity<User> registerUser(@RequestBody User newUser) {

        // generate apiKey
        newUser.setApiKey(UUID.randomUUID().toString());

        User savedUser = userRepo.save(newUser);
        return new ResponseEntity<User>(savedUser,HttpStatus.CREATED);
    }

    //Gets user based on his id
    @GetMapping("/user")
    public ResponseEntity<User> getUserById(@RequestParam(value = "id") int id) {
        Optional<User> savedUser = userRepo.findById(id);

        if(savedUser.isPresent()) {
            return new ResponseEntity<User>(savedUser.get(), HttpStatus.OK);

        }
        return new ResponseEntity("Error, no user found with id " + id, HttpStatus.NOT_FOUND);
    }

    //Gets all users in the database
    @GetMapping("/user/all")
    public ResponseEntity<Iterable<User>> getAllUsers() {

        Iterable<User> usersInDb = userRepo.findAll();
        return new ResponseEntity<Iterable<User>>(usersInDb, HttpStatus.OK);
    }

    //Creates an api key for coummication so the information of email and password doesn't get sent around
    @GetMapping("/validate")
    public ResponseEntity<String> validateUser(@RequestParam(value = "email") String email,
                                             @RequestParam(value = "password") String password) {

            var validUser = userRepo.findByEmailAndPassword(email, password);
            if(validUser.isPresent()){
                return new ResponseEntity<String>("Api key: " + validUser.get().getApiKey(), HttpStatus.OK);
            }

            return new ResponseEntity<>("Wrong credentials", HttpStatus.UNAUTHORIZED);

    }


}
