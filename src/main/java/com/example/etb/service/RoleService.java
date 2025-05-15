package com.example.etb.service;


import com.example.etb.model.RoleModel;
import com.example.etb.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    // Create a new role
    public RoleModel addRole(String roleName) {
        if (roleRepository.findByName(roleName).isPresent()) {
            throw new RuntimeException("Role already exists!");
        }

        RoleModel role = new RoleModel();
        role.setName(roleName.toUpperCase());
        return roleRepository.save(role);
    }

    // Get all roles
    public List<RoleModel> getAllRoles() {
        return roleRepository.findAll();
    }

    public RoleModel getRole(String roleName) {
        return roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found!" + roleName));
    }
    // Update role name
    public RoleModel updateRole(Long id, String newName) {
        RoleModel role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found!"));

        role.setName(newName.toUpperCase());
        return roleRepository.save(role);
    }

    // Delete role
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

//    public List<Permission> findPermissionsByRoleId(Long roleId) {
//        return rolePermissionRepository.findPermissionsByRoleId(roleId); // Call method on instance, not statically
//    }

    // Fetch role by ID
    public RoleModel findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }
}
