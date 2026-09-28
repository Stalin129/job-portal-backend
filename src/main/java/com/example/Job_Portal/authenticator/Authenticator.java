package com.example.Job_Portal.authenticator;

import com.example.Job_Portal.userEntity.LoginRequest;
import com.example.Job_Portal.userEntity.UpdateUser;
import com.example.Job_Portal.userEntity.User;
import com.example.Job_Portal.userEntity.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Authenticator {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    Authenticator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(User data) {
        if (data.getUsername().length() < 8 || data.getPassword().length() < 8) {
            return "Username/password must greater 8 letter";
        } else if (userRepository.findByUsername(data.getUsername()).isPresent()) {
            return "Username must unique";
        }
        data.setPassword(passwordEncoder.encode(data.getPassword()));
        userRepository.save(data);
        return "Account created";
    }

    public User login(LoginRequest data) {
        User user = userRepository.findByUsername(data.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
    public String verification(String username,String password){
        try{
        Optional<User> user =userRepository.findByUsername(username);
        if(user.isPresent()){
            boolean matches=passwordEncoder.matches(password,user.get().getPassword());
            if(matches){
                return "Verified";
            }
        }
            throw new RuntimeException("Not verified");
    } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public String userupdate(UpdateUser data) {
        try {
            if (userRepository.findByUsername(data.getUsername()).isPresent()) {
                Optional<User> user = userRepository.findByUsername(data.getUsername());
                User users = user.get();
                if (!data.getUpdatename().isEmpty()) {
                    if(!userRepository.findByUsername(data.getUpdatename()).isPresent()){
                        users.setUsername(data.getUpdatename());
                        userRepository.save(users);
                        return "Succesfully Updated";
                    }
                    else {
                        return "Username must unique";
                    }

                } else if (!data.getEmail().isEmpty()) {
                    users.setEmail(data.getEmail());
                    userRepository.save(users);
                    return "Succesfully Updated";
                } else if (!data.getPassword().isEmpty()) {
                    users.setPassword(passwordEncoder.encode(data.getPassword()));
                    userRepository.save(users);
                    return "Succesfully Updated";
                }

            }
            throw new RuntimeException("Update Failed");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

}
