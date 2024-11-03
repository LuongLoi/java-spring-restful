package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.dto.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {

    private PermissionRepository permissionRepository;
    private RoleRepository roleRepository;

    public RoleService(PermissionRepository permissionRepository,
        RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    public Role handleCreateRole(Role role) {
        // check permissions
        if (role.getPermissions() != null) {
            List<Long> permissionIds = role.getPermissions()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(permissionIds);
            role.setPermissions(dbPermissions);
        }

        Role currentRole = this.roleRepository.save(role);

        return currentRole;
    }

    public Role handleUpdateRole(Role role) {
        Role r = this.fetchRoleById(role.getId());

        // check permissions
        if (role.getPermissions() != null) {
            List<Long> permissionIds = role.getPermissions()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(permissionIds);
            role.setPermissions(dbPermissions);
            r.setPermissions(dbPermissions);
        }

        r.setName(role.getName());
        r.setActive(role.isActive());
        r.setDescription(role.getDescription());

        Role currentRole = this.roleRepository.save(r);

        return currentRole;
    }

    public Role fetchRoleById(Long id) {
        return this.roleRepository.findById(id).isPresent() ? this.roleRepository.findById(id).get() : null;
    }

    public ResultPaginationDTO handleFetchAllRole(Specification<Role> spec, Pageable pageable) {
        Page<Role> rolePage = this.roleRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(rolePage.getTotalPages());
        meta.setTotal(rolePage.getTotalElements());
        rs.setMeta(meta);

        rs.setResult(rolePage.getContent());
        return rs;
    }

     public void delete(long id) {
        this.roleRepository.deleteById(id);
    }
}
