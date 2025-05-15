package com.example.etb.service;

import com.example.etb.model.RoleModel;
import com.example.etb.model.UserModel;
import com.example.etb.repository.RoleRepository;
import com.example.etb.repository.UserRepository;
import com.example.etb.util.EmailUtil;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private PasswordEncoder passwordEncoder; // For secure password storage

    @Autowired
    private RoleService roleService;

    public UserModel registerUser(UserModel user, String roleName) {

        RoleModel role = roleService.getRole(roleName);

        String subject = "Welcome to Our Event System, " + user.getName() + "!";
        String body = "Dear " + user.getName() + ",\n\n" + "Congratulations on successfully registering with our event system!\n\n" + "Here are your account details:\n\n" + "Username: " + user.getEmail() + "\n" + "Password: " + user.getPassword() + "\n\n" + // You should consider hashing passwords and not sending them as plain text.
                "Please keep your credentials safe and do not share them with anyone.\n\n" + "Best Regards,\nThe Event Team";

        // Send the registration confirmation email
        emailUtil.sendSimpleEmail(user.getEmail(), subject, body);
        if (role == null) {
            throw new IllegalArgumentException("Invalid role: " + roleName);
        }
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Return the saved user
        return userRepository.save(user);
    }


    public UserModel getUserById(Long userId) {
        Optional<UserModel> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    // Fetch all organizations. Assuming that organizations are identified by a specific role (e.g., "ROLE_ORG").
    public List<UserModel> getAllOrganizations(String RoleName) {
        return userRepository.findByRole_Name(RoleName); // Modify according to your role system.
    }


}