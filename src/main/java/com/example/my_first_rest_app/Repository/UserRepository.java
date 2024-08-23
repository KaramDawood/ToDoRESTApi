package com.example.my_first_rest_app.Repository;

import com.example.my_first_rest_app.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

            //to validate the user
            Optional<User> findByEmailAndPassword(String email, String password);

            //to validate the apiKey
            Optional<User> findByApiKey(String apiKey);

}
