package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public Iterable<User> fetchAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> fetchUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public void insertUser(User user) {
        if (!checkUser(user)) {
            userRepository.save(user);
        } else throw new RuntimeException("User already exists!");
    }

    @Override
    public boolean checkUser(User user) {
        return user.getId() != 0 && userRepository.existsById(user.getId());
    }

    @Override
    public void updateUser(User user) {
        if (checkUser(user)) {
            userRepository.save(user);
        } else throw new RuntimeException("User does not exist!");
    }

    @Override
    public void deleteUser(User user) {
        if (checkUser(user)) {
            userRepository.deleteById(user.getId());
        } else throw new RuntimeException("User does not exist!");
    }

    @Override
    public List<User> fetchUserByAccountStatus(String status) {
        return getStreamFromIterable(userRepository.findAll())
                .filter(u -> status.equals(u.getAccountStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public long totalUserCount(){
        return userRepository.count();
    }

    @Override
    public long totalCountOfPendingApprovals(){
        return userRepository.countUserByAccountStatus(User.AccountStatus.valueOf("Pending"));
    }

    //users whose approval is pending


    private static <T> Stream<T> getStreamFromIterable(Iterable<T> iterable) {
        Spliterator<T> spliterator = iterable.spliterator();
        return StreamSupport.stream(spliterator, false);
    }
}
