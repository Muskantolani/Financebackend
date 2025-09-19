package org.example.apitestingproject;

import org.example.apitestingproject.entities.Admin;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.repository.AdminRepository;
import org.example.apitestingproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class AdminTest {

    @Autowired private UserRepository userRepository;
    @Autowired private AdminRepository adminRepository;

    @Test
    void syncAdminsFromUsers() {
        Iterable<User> adminUsers = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == User.Role.Super_Admin || u.getRole() == User.Role.Sub_Admin)
                .toList();


        for (User u : adminUsers) {
            Admin.Role role = (u.getRole() == User.Role.Super_Admin) ?
                    Admin.Role.SUPER_ADMIN : Admin.Role.SUB_ADMIN;

            Admin admin = new Admin(
                    u.getId(),            // SAME as USER_ID
                    u.getUsername(),
                    u.getPassword(),
                    role
            );

            adminRepository.save(admin);
        }
    }
}