package org.example.apitestingproject.service;

import org.example.apitestingproject.dto.AdminDto;
import org.example.apitestingproject.dto.UserDto;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.entities.Admin;
import org.example.apitestingproject.repository.AdminRepository;
import org.example.apitestingproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Admin>  fetchAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public List<Admin> fetchAdminsByRole(Admin.Role role) {
        return adminRepository.findByRole(role);
    }
    @Override
    public Admin addSubAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> fetchAdminById(Integer id) {
        return adminRepository.findById(id);
    }


    @Override
    public List<AdminDto> fetchSubAdminsForFrontend() {
        List<Admin> admins = adminRepository.findByRole(Admin.Role.SUB_ADMIN);
        List<AdminDto> response = new ArrayList<>();
        for (Admin a : admins) {
            response.add(new AdminDto(
                    a.getId(),
                    a.getUsername(),      // make sure Admin entity has 'name'
                    a.getRole().toString()
            ));
        }
        return response;
    }

    @Override
    public List<Admin> fetchSubAdmins() {
        return adminRepository.findByRole(Admin.Role.SUB_ADMIN);
    }

    @Override
    public void insertAdmin(Admin admin) {
        if (!checkAdmin(admin)) {
            adminRepository.save(admin);
        } else {
            throw new RuntimeException("Admin already exists!");
        }
    }

    @Override
    public boolean checkAdmin(Admin admin) {
        return adminRepository.findByUsernameAndPassword(admin.getUsername(), admin.getPassword())
                .isPresent();
    }

    @Override
    public UserDto updateUserFromDto(int id, UserDto dto) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        User user = optionalUser.get();

        // update fields
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        // map string status → enum
        switch (dto.getStatus()) {
            case "Active" -> user.setAccountStatus(User.AccountStatus.Activated);
            case "Pending" -> user.setAccountStatus(User.AccountStatus.Pending);
            case "Rejected" -> user.setAccountStatus(User.AccountStatus.Rejected);
        }

        userRepository.save(user);

        // map back to DTO
        String cardType = (user.getPreferredCardType() != null)
                ? user.getPreferredCardType().getName().toString()
                : "-";

        return new UserDto(
                user.getId(),          // dbId
                dto.getDisplayId(),    // displayId
                user.getName(),
                user.getEmail(),
                dto.getStatus(),
                cardType
        );
    }

    @Override
    public List<UserDto> getAllUsersForFrontend() {
        List<User> users = userRepository.findAll();
        List<UserDto> response = new ArrayList<>();
        int counter = 1;

        for (User user : users) {
            String displayId = "U" + String.format("%03d", counter++);

            String status = switch (user.getAccountStatus()) {
                case Activated -> "Active";
                case Pending -> "Pending";
                case Rejected -> "Rejected";
            };

            String cardType = (user.getPreferredCardType() != null)
                    ? user.getPreferredCardType().getName().toString()
                    : "-";

            response.add(new UserDto(
                    user.getId(),       // real DB USER_ID
                    displayId,          // pretty one
                    user.getName(),
                    user.getEmail(),
                    status,
                    cardType
            ));
        }

        return response;
    }

    @Override
    public void updateAdmin(Admin admin) {
        if (admin.getId() != 0 && adminRepository.existsById(admin.getId())) {
            adminRepository.save(admin);
        } else {
            throw new RuntimeException("Admin does not exist!");
        }
    }

    @Override
    public void deleteAdmin(Admin admin) {
        if (admin.getId() != 0 && adminRepository.existsById(admin.getId())) {
            adminRepository.deleteById(admin.getId());
        } else {
            throw new RuntimeException("Admin does not exist!");
        }
    }

    @Override
    public Long GetUserCount() {
        return userRepository.count();
    }

    @Override
    public void deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User does not exist!");
        }
    }



    // users whose approval is pending
    @Override
    public List<User> FindPendingApprovals() {
        return userRepository.findByAccountStatus(User.AccountStatus.Pending);
    }

    @Override
    public long countByApprovalStatus(User.AccountStatus status) {
        return userRepository.countUserByAccountStatus(status);
    }
}
