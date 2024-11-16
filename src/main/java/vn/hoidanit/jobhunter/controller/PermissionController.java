package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
    
    @PostMapping("/permissions")
    @ApiMessage("Create new permissons")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission p) 
            throws IdInvalidException {
        //TODO: process POST request
        if (this.permissionService.checkExistPermission(p) == true) {
            throw new IdInvalidException("Permission đã tồn tại!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.handleCreatePermission(p));
    }
    
    @PutMapping("/permissions")
    @ApiMessage("Update permissions")
    public ResponseEntity<Permission> updatePermisson(@Valid @RequestBody Permission p) 
            throws IdInvalidException {
        if (this.permissionService.handleFetchPermissionById(p.getId()) == null)
            throw new IdInvalidException("Permission với id = " + p.getId() + " không tồn tại!");

        // check exist by module, apiPath and method
        if (this.permissionService.checkExistPermission(p) || this.permissionService.isSameName(p)) {
            throw new IdInvalidException("Permission đã tồn tại.");
            // check name
        }


        return ResponseEntity.ok(this.permissionService.handleUpdatePermisson(p));
    }
    
    @GetMapping("/permissions/{id}")
    @ApiMessage("Fetch permission by id")
    public ResponseEntity<Permission> fetchPermissionById(@PathVariable("id") Long id) 
            throws IdInvalidException {
        Permission p = this.permissionService.handleFetchPermissionById(id);
        if (p == null)
            throw new IdInvalidException("Permission với id = " + id + " không tồn tại!");
        return ResponseEntity.ok(p);
    }
    
    @GetMapping("/permissions")
    @ApiMessage("Fetch all permissions")
    public ResponseEntity<ResultPaginationDTO> fetchAllPermissions(@Filter Specification<Permission> spec, Pageable pageable) {
        return ResponseEntity.ok(this.permissionService.handleFetchAllPermission(spec, pageable));
    }
    
    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete permission by id")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") long id) 
        throws IdInvalidException {
        Permission permission = this.permissionService.handleFetchPermissionById(id);
        if (permission == null) 
            throw new IdInvalidException("Permission với id = " + id + " không tồn tại!");
        this.permissionService.handleDeletePermissions(id);
        return ResponseEntity.ok(null);
    }
    
}
