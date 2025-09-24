package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Iterable<User> fetchAllUser();

    Optional<User> fetchUserById(Integer id);

    void insertUser(User user);

    boolean checkUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    List<User> fetchUserByAccountStatus(String status);

    long totalUserCount();

    long totalCountOfPendingApprovals();
}
