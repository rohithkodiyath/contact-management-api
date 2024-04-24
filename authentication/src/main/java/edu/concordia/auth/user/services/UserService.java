package edu.concordia.auth.user.services;

import edu.concordia.auth.exception.UsernameTakenException;
import edu.concordia.auth.user.entity.AppUser;
import edu.concordia.auth.user.model.UserModel;
import edu.concordia.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        return userRepository.findByEmailAddress(emailAddress);
    }

    public AppUser loggedInUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (AppUser) principal;
    }

    public AppUser loadUserByUserUuid(String userUuid)  {
        return userRepository.findUserByUuid(userUuid);
    }

    public AppUser createUser(UserModel userModel) throws UsernameTakenException {
        if (userRepository.findByEmailAddress(userModel.getEmailAddress()) != null) {
            throw new UsernameTakenException("Username already taken");
        }
        if(!userModel.getPassword().equals(userModel.getConfirmPassword())) {
            //throw new MethodArgumentNotValidException("Passwords do not match");
            throw new IllegalArgumentException("Passwords do not match");
        }
        var entity = userModel.toEntity();
        entity.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userRepository.save(entity);
    }
}
