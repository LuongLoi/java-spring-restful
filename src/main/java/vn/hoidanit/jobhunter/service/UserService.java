package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserFetchDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserUpdateDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User handleCreateUser(User user) {
        //check company
        if (user.getCompany() != null) {
            Optional<Company> company = this.companyService.findById(user.getCompany().getId());
            user.setCompany(company.isPresent() ? company.get() : null);
        }
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
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        List<ResUserFetchDTO> listUser = pageUser.getContent()
        .stream().map(item -> new ResUserFetchDTO(
            item.getId(),
            item.getName(),
            item.getEmail(),
            item.getAge(),
            item.getGender(),
            item.getAddress(),
            item.getCreatedAt(),
            item.getUpdatedAt(),
            new ResUserFetchDTO.CompanyUser(
                item.getCompany() != null ? item.getCompany().getId() : 0,
                item.getCompany() != null ? item.getCompany().getName() : "")                
            ))
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
            if (updateUser.getCompany() != null) {
                Optional<Company> company = this.companyService.findById(updateUser.getCompany().getId());
                user.setCompany(company.isPresent() ? company.get() : null);
            }
        }
        return this.userRepository.save(user);
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
    
    public boolean isExistEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResUserCreateDTO convertToResUserCreateDTO(User user) {
        ResUserCreateDTO userCreateDTO = new ResUserCreateDTO();
        userCreateDTO.setId(user.getId());
        userCreateDTO.setName(user.getName());
        userCreateDTO.setEmail(user.getEmail());
        userCreateDTO.setGender(user.getGender());
        userCreateDTO.setAddress(user.getAddress());
        userCreateDTO.setAge(user.getAge());
        userCreateDTO.setCreatedAt(user.getCreatedAt());
        ResUserCreateDTO.CompanyUser companyUser = new ResUserCreateDTO.CompanyUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
        }
        userCreateDTO.setCompany(companyUser);
        
        return userCreateDTO;
    }

    public ResUserFetchDTO convertToResUserFetchDTO(User user) {
        ResUserFetchDTO userFetchDTO = new ResUserFetchDTO();
        userFetchDTO.setId(user.getId());
        userFetchDTO.setName(user.getName());
        userFetchDTO.setEmail(user.getEmail());
        userFetchDTO.setAddress(user.getAddress());
        userFetchDTO.setAge(user.getAge());
        userFetchDTO.setGender(user.getGender());
        userFetchDTO.setCreatedAt(user.getCreatedAt());
        userFetchDTO.setUpdateAt(user.getUpdatedAt());
        ResUserFetchDTO.CompanyUser companyUser = new ResUserFetchDTO.CompanyUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
        }
        userFetchDTO.setCompanyUser(companyUser);
        return userFetchDTO;
    }

    public ResUserUpdateDTO convertToResUserUpdateDTO(User user) {
        ResUserUpdateDTO userUpdateDTO = new ResUserUpdateDTO();
        userUpdateDTO.setId(user.getId());
        userUpdateDTO.setAddress(user.getAddress());
        userUpdateDTO.setName(user.getName());
        userUpdateDTO.setAge(user.getAge());
        userUpdateDTO.setGender(user.getGender());
        userUpdateDTO.setUpdatedAt(user.getUpdatedAt());
        ResUserUpdateDTO.CompanyUser companyUser = new ResUserUpdateDTO.CompanyUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
        }
        userUpdateDTO.setCompanyUser(companyUser);
        return userUpdateDTO;
    }

    public void updateUserToken(String token, String email) {
        User user = this.handleGetUserByUsername(email);
        if (user != null) {
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }

    public User handleFindUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
