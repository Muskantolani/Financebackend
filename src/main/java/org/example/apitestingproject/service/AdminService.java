package org.example.apitestingproject.service;
import org.example.apitestingproject.dto.AdminDto;
import org.example.apitestingproject.dto.UserDto;
import org.example.apitestingproject.entities.Admin;
import org.example.apitestingproject.entities.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public interface AdminService {

    List<Admin> fetchAllAdmins();

    List<Admin> fetchAdminsByRole(Admin.Role role);

    Optional<Admin> fetchAdminById(Integer id);
    Admin addSubAdmin(Admin admin);
    void insertAdmin(Admin admin);

    boolean checkAdmin(Admin admin);
    List<AdminDto> fetchSubAdminsForFrontend();
    void updateAdmin(Admin admin);

    void deleteAdmin(Admin admin);

    Long GetUserCount();
    List<Admin> fetchSubAdmins();
    //users whose approval is pending
    List<User>FindPendingApprovals();
    UserDto updateUserFromDto(int id, UserDto dto);
    List<UserDto> getAllUsersForFrontend();
    void deleteUser(Integer id);
    long countByApprovalStatus(User.AccountStatus status);
}



