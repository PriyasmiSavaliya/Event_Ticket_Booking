package com.example.etb.controller;


import com.example.etb.Constant.Config.PathConstant;
import com.example.etb.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(PathConstant.ROLES_PATH)
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Show all roles
    @GetMapping
    public String getAllRoles(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "roles/roles";
    }

    // Add a new role
    @PostMapping("/add")
    public String addRole(@RequestParam String roleName) {
        roleService.addRole(roleName);
        return "redirect:/" + PathConstant.ROLES_PATH;
    }

    // Update an existing role
    @PostMapping("/update")
    public String updateRole(@RequestParam Long roleId, @RequestParam String roleName) {
        roleService.updateRole(roleId, roleName);
        return "redirect:/" + PathConstant.ROLES_PATH;
    }

    // Delete a role
    @PostMapping("/delete")
    public String deleteRole(@RequestParam Long roleId) {
        roleService.deleteRole(roleId);
        return "redirect:/" + PathConstant.ROLES_PATH;
    }


}
