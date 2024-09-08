package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.UserCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.UserFetchDTO;
import vn.hoidanit.jobhunter.domain.dto.UserUpdateDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleGetUserById(long id) {
        User user = this.userRepository.findById(id).isPresent() ? this.userRepository.findById(id).get() : null;
        return user;
    }

    public ResultPaginationDTO handleGetAllUsers(Specification<User> spec ,Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
            
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        List<UserFetchDTO> listUser = pageUser.getContent()
        .stream().map(item -> new UserFetchDTO(
            item.getId(),
            item.getName(),
            item.getEmail(),
            item.getAge(),
            item.getGender(),
            item.getAddress(),
            item.getCreatedAt(),
            item.getUpdatedAt()))
        .collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }

    public User handleUpdateUser(User updateUser) {
        User user = this.handleGetUserById(updateUser.getId());
        
        if (user != null) {
            user.setAddress(updateUser.getAddress());
            user.setName(updateUser.getName());
            user.setAge(updateUser.getAge());
            user.setGender(updateUser.getGender());
        }
        return this.userRepository.save(user);
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
    
    public boolean isExistEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public UserCreateDTO convertToResUserCreateDTO(User user) {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setId(user.getId());
        userCreateDTO.setName(user.getName());
        userCreateDTO.setEmail(user.getEmail());
        userCreateDTO.setGender(user.getGender());
        userCreateDTO.setAddress(user.getAddress());
        userCreateDTO.setAge(user.getAge());
        userCreateDTO.setCreatedAt(user.getCreatedAt());
        return userCreateDTO;
    }

    public UserFetchDTO convertToResUserFetchDTO(User user) {
        UserFetchDTO userFetchDTO = new UserFetchDTO();
        userFetchDTO.setId(user.getId());
        userFetchDTO.setName(user.getName());
        userFetchDTO.setEmail(user.getEmail());
        userFetchDTO.setAddress(user.getAddress());
        userFetchDTO.setAge(user.getAge());
        userFetchDTO.setGender(user.getGender());
        userFetchDTO.setCreatedAt(user.getCreatedAt());
        userFetchDTO.setUpdateAt(user.getUpdatedAt());
        return userFetchDTO;
    }

    public UserUpdateDTO convertToResUserUpdateDTO(User user) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setId(user.getId());
        userUpdateDTO.setAddress(user.getAddress());
        userUpdateDTO.setName(user.getName());
        userUpdateDTO.setAge(user.getAge());
        userUpdateDTO.setGender(user.getGender());
        userUpdateDTO.setUpdatedAt(user.getUpdatedAt());
        return userUpdateDTO;
    }

    public void updateUserToken(String token, String email) {
        User user = this.handleGetUserByUsername(email);
        if (user != null) {
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }
}
