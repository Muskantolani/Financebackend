package org.example.apitestingproject.controller;

import org.example.apitestingproject.dto.AdminDto;
import org.example.apitestingproject.dto.UserDto;
import org.example.apitestingproject.entities.Admin;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.service.AdminService;
import org.example.apitestingproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.PSource;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    public AdminController() {
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable int id,
            @RequestBody UserDto dto) {

        try {
            UserDto updatedUser = adminService.updateUserFromDto(id, dto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
//    @GetMapping("/subadmins")
//    public List<AdminDto> getSubAdmins() {
//        System.out.println("gggggggggggggggggggggggggggggggggg");
//        return adminService.fetchSubAdmins()
//                .stream()
//                .map(admin -> new AdminDto(admin.getId(), admin.getUsername(), admin.getRole().name()))
//                .toList();
//    }

    @PostMapping("/subadmin")
    public ResponseEntity<Admin> addSubAdmin(@RequestBody Admin newAdmin) {
        Admin savedAdmin = adminService.addSubAdmin(newAdmin);
        return ResponseEntity.ok(savedAdmin);
    }

    @GetMapping("/subadmins")
    public List<AdminDto> getSubAdmins() {
        return adminService.fetchSubAdminsForFrontend();
    }

    @DeleteMapping("/subadmins/{id}")
    public void deleteSubAdmin(@PathVariable int id) {
        Optional<Admin> admin = adminService.fetchAdminById(id);
        if (admin.isPresent()) {
            adminService.deleteAdmin(admin.get());
        } else {
            throw new RuntimeException("Admin not found");
        }
    }

//
//    @GetMapping("/all")
//    public Iterable<Admin> getAllAdmins() {
//        return adminService.fetchAllAdmins();
//    }
//
//    @GetMapping("/role/{role}")
//    public List<Admin> getAdminsByRole(@PathVariable String role) {
//        return adminService.fetchAdminsByRole(role);
//    }

    @GetMapping("/{id}")
    public Optional<Admin> getAdminById(@PathVariable Integer id) {
        return adminService.fetchAdminById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAdmin(@RequestBody Admin admin) {
        try {
            adminService.insertAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin created!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/getusercount")
    public Long getAllUserCount(){
        return adminService.GetUserCount();
    }
    @GetMapping("/getpendingaccounts")
    public void getPendingUserCount(){
        adminService.FindPendingApprovals();
    }
    @GetMapping("/getpendingcount")
    public Long  getPendingCount(){
        return adminService.countByApprovalStatus(User.AccountStatus.valueOf("Pending"));
    }
    @GetMapping("/getactivecount")
    public Long  getActiveCount(){
        return adminService.countByApprovalStatus(User.AccountStatus.valueOf("Activated"));
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            adminService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/allUsers")
    public List<UserDto> getAllUsers() {
        return adminService.getAllUsersForFrontend();
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateAdmin(@RequestBody Admin admin) {
        try {
            adminService.updateAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin updated!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAdmin(@RequestBody Admin admin) {
        try {
            adminService.deleteAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin deleted!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}