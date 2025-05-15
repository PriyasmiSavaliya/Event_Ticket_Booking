package com.example.etb.controller;

import com.example.etb.model.RoleModel;
import com.example.etb.model.UserModel;
import com.example.etb.service.RoleService;
import com.example.etb.service.UserService;
import com.example.etb.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    @Autowired
    private EmailUtil emailUtil;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("roles", roleService.getAllRoles()); // Fetch all roles dynamically
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserModel user, @RequestParam String roleName) {


//        // Construct email subject and body
//        String subject = "Welcome to Our System, " + firstName + "!";
//        String body = "Dear " + firstName + " " + lastName + ",\n\n" + "Welcome to our system. Here are your account details:\n\n" + "Username: " + username + "\n" + "Password: " + password + "\n\n" + "Please keep these credentials safe and do not share them with anyone.\n\n" + "Best Regards,\nYour Team";
//
//        // Send email
//        emailUtil.sendSimpleEmail(email, subject, body);


        userService.registerUser(user , roleName);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "/login";
    }


}
