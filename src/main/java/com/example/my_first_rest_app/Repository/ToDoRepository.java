package com.example.my_first_rest_app.Repository;

import com.example.my_first_rest_app.Model.ToDo;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ToDoRepository extends CrudRepository<ToDo, Integer> {

            Set<ToDo> findAllByUserId(int userId);

}
