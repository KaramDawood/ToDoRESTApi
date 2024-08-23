package com.example.my_first_rest_app.Controller;

import com.example.my_first_rest_app.Model.ToDo;
import com.example.my_first_rest_app.Model.User;
import com.example.my_first_rest_app.Repository.ToDoRepository;
import com.example.my_first_rest_app.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ToDoController {

    @Autowired
    private ToDoRepository toDoRepo;
    @Autowired
    private UserRepository userRepo;

    //Gets a todo by id
    @GetMapping("/todo")
    public ResponseEntity<ToDo> getToDoById(@RequestParam(value = "id") int id){

        Optional<ToDo> toDoOptional = toDoRepo.findById(id);
       if(toDoOptional.isPresent()){
           return new ResponseEntity<ToDo>(toDoOptional.get(), HttpStatus.OK);
       }else {
           return new ResponseEntity("No todo found with id " + id, HttpStatus.NOT_FOUND);
       }
    }

    //Gets all todos (by a generated apiKey)
    @GetMapping("/todo/all")
    public ResponseEntity<Iterable<ToDo>> getAllToDos(@RequestHeader(name = "apiKey") String apiKey){

        System.out.println(apiKey);
        Optional<User> userRepoByApiKey = userRepo.findByApiKey(apiKey);

        if(userRepoByApiKey.isPresent()){
            Iterable<ToDo> toDosInDb = toDoRepo.findAllByUserId(userRepoByApiKey.get().getId());
            return new ResponseEntity<Iterable<ToDo>>(toDosInDb, HttpStatus.OK);
        }
        return new ResponseEntity("Invalid Api Key", HttpStatus.NOT_FOUND);

    }

    //Creates a todo
    @PostMapping("/todo")
    public ResponseEntity<ToDo> createToDo(@RequestBody ToDo todo){
        toDoRepo.save(todo);
        return new ResponseEntity<ToDo>(todo, HttpStatus.CREATED);
    }

    //Deletes a todo by id
    @DeleteMapping("/todo")
    public ResponseEntity<String> deleteToDoById(@RequestParam(value = "id") int id){
        Optional<ToDo> toDoOptional = toDoRepo.findById(id);
        if(toDoOptional.isPresent()){
            toDoRepo.delete(toDoOptional.get());
            return new ResponseEntity<String>("Successfully deleted todo with id: " + id, HttpStatus.OK);
        }else{
            return new ResponseEntity<String>("No todo found to delete with id " + id, HttpStatus.NOT_FOUND);
        }

    }

    //Updates a todo
    @PutMapping("/todo")
    public ResponseEntity<ToDo> updateToDo(@RequestBody ToDo updateTodo){

        Optional<ToDo> toDoOptional = toDoRepo.findById(updateTodo.getId());

        if(toDoOptional.isPresent()){
            ToDo updatedToDo = toDoRepo.save(updateTodo);
            return new ResponseEntity<ToDo>(updatedToDo, HttpStatus.OK);
        }
            return new ResponseEntity("No todo found to update with id " + updateTodo.getId(), HttpStatus.NOT_FOUND);
    }

    //Changes the stae of setDone of a todo activity to either true or false
    @PatchMapping("/todo/setDone")
    public ResponseEntity<ToDo> setDoneToDo(@RequestParam(value = "isDone") boolean isDone,
                                            @RequestParam(value = "id") int id){

        Optional<ToDo> toDoOptional = toDoRepo.findById(id);

        if(toDoOptional.isPresent()){
            toDoOptional.get().setIsDone(isDone);
            ToDo savedToDo = toDoRepo.save(toDoOptional.get());
            return new ResponseEntity<ToDo>(savedToDo, HttpStatus.OK);
        }
        return new ResponseEntity("No todo found to update with id " + id, HttpStatus.NOT_FOUND);

    }

    //Deletes all todos
    @DeleteMapping("/todo/deleteAll")
    public ResponseEntity<String> deleteAllToDo(){
        toDoRepo.deleteAll();
        return new ResponseEntity<String>("Successfully deleted all todos", HttpStatus.OK);
    }

}
