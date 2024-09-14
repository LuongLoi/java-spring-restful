package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserFetchDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserUpdateDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.EmailInvalidException;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController { 

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create new user")
    public ResponseEntity<ResUserCreateDTO> handeCreateUser(@Valid @RequestBody User postManUser)
            throws EmailInvalidException {
        boolean isExistEmail = this.userService.isExistEmail(postManUser.getEmail());
        if (isExistEmail)
            throw new EmailInvalidException(
                    "Email " + postManUser.getEmail() + " đã tồn tại, vui lòng sử dụng email khác!");
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User user = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResUserCreateDTO(user));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User user = this.userService.handleGetUserById(id);
        if (user == null)
            throw new IdInvalidException("ID không tồn tại!");
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch user by id")
    public ResponseEntity<ResUserFetchDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User user = this.userService.handleGetUserById(id);
        if (user == null)
            throw new IdInvalidException("ID không tồn tại!");
        return ResponseEntity.ok(this.userService.convertToResUserFetchDTO(user));
    }

    // fetch all users
    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
        @Filter Specification<User> spec, Pageable pageable
    ) {
        return ResponseEntity.ok( this.userService.handleGetAllUsers(spec, pageable));
    }

    @PutMapping("/users")
    public ResponseEntity<ResUserUpdateDTO> handleUpdateUser(@RequestBody User updateUser) throws IdInvalidException{
        
        User user = this.userService.handleGetUserById(updateUser.getId());
        if (user == null)
            throw new IdInvalidException("ID = " + updateUser.getId()+ " không tồn tại!");
        user = this.userService.handleUpdateUser(updateUser);
        return ResponseEntity.ok(this.userService.convertToResUserUpdateDTO(user)); 
    }
}
