package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String handeCreateUser() {
        User user = new User();
        user.setEmail("user01@gmail.com");
        user.setPassword("123456");
        user.setName("user 01");
        this.userService.handleCreateUser(user);
        return "User";
    }
}
