package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> handeCreateUser(@RequestBody User postManUser) {
        User user = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<String> handleIdException(IdInvalidException idException) {
        return ResponseEntity.badRequest().body(idException.getMessage());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {

        if (id >= 1500) {
            throw new IdInvalidException("Id khong lon hon 1501");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok("User eric");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.userService.handleGetUserById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.userService.handleGetAllUsers());
    }

    @PutMapping("/users")
    public ResponseEntity<User> handeUpdateUser(@RequestBody User updateUser) {
        User user = this.userService.handleUpdateUser(updateUser);
        return ResponseEntity.ok(user); 
    }
}
