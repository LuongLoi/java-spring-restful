package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean checkExistPermission(Permission p) {
        return this.permissionRepository.existsByApiPathAndMethodAndModule(p.getApiPath(), p.getMethod(),
                p.getModule());
    }

    public Permission handleFetchPermissionById(Long id) {
        return this.permissionRepository.findById(id).isPresent() 
            ? this.permissionRepository.findById(id).get() : null ;
    }
    
    public Permission handleCreatePermission(Permission p) {
        return this.permissionRepository.save(p);
    }

    public Permission handleUpdatePermisson(Permission p) {
        Permission permission = this.handleFetchPermissionById(p.getId());
        if (permission != null) {
            permission.setApiPath(p.getApiPath());
            permission.setName(p.getName());
            permission.setMethod(p.getMethod());
            permission.setModule(p.getModule());
            return this.permissionRepository.save(permission);
        }
        return null;
    }

    public ResultPaginationDTO handleFetchAllPermission(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> p = this.permissionRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(p.getTotalPages());
        meta.setTotal(p.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(p.getContent());
        return rs;
    }

    public void handleDeletePermissions(long id) {
         // delete permissions (inside permission_role table)
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        //delete permission
        this.permissionRepository.delete(currentPermission);
    }
}
