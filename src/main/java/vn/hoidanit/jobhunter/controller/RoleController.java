package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private RoleRepository roleRepository;
    private RoleService roleService;

    public RoleController(RoleRepository roleRepository, RoleService roleService) {
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create new Roles")
    public ResponseEntity<Role> createRoles(@Valid @RequestBody Role role) 
        throws IdInvalidException {
        //TODO: process POST request
        if (this.roleRepository.existsByName(role.getName()) == true)
            throw new IdInvalidException("Role " + role.getName() + " đã tồn tại!");
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.handleCreateRole(role));
    }
    
    @PutMapping("/roles")
    @ApiMessage("Update Roles")
    public ResponseEntity<Role> updateRoles(@Valid @RequestBody Role role) 
            throws IdInvalidException {
        //TODO: process PUT request
        if (this.roleService.fetchRoleById(role.getId()) == null)
            throw new IdInvalidException("Role với id = " + role.getId() + " không tồn tại!");
        if (this.roleRepository.existsByName(role.getName()) == true
                && this.roleRepository.findByName(role.getName()).getId() != role.getId())
            throw new IdInvalidException("Role " + role.getName() + " đã tồn tại!");
        return ResponseEntity.ok(this.roleService.handleUpdateRole(role));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch Role by Id")
    public ResponseEntity<Role> fetchRoleById(@PathVariable("id") Long id) 
            throws IdInvalidException {
        if (this.roleService.fetchRoleById(id) == null)
            throw new IdInvalidException("Role với id = " + id + " không tồn tại!");
        return ResponseEntity.ok(this.roleService.fetchRoleById(id));
    }
    
    @GetMapping("/roles")
    @ApiMessage("Fetch All Roles")
    public ResponseEntity<ResultPaginationDTO> fetchAllRoles(@Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.ok(this.roleService.handleFetchAllRole(spec, pageable));
    }
    
    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete Role by ID")
    public ResponseEntity<Void> deleteRoleById(@PathVariable("id") Long id) throws IdInvalidException{
        if (this.roleService.fetchRoleById(id) == null)
            throw new IdInvalidException("Role với id = " + id + " không tồn tại!");
        this.roleService.delete(id);
        return ResponseEntity.ok(null);
    }
}
