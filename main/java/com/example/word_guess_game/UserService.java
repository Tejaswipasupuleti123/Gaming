package com.example.word_guess_game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateScore(Long userId, int newScore) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setScore((long) newScore);
            userRepository.save(user);
        } else {
            // Handle user not found scenario
        }
    }


}
