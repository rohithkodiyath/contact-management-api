package edu.concordia.auth.user.controller;

import edu.concordia.auth.exception.UserNotFoundException;
import edu.concordia.auth.model.request.LoginRequest;
import edu.concordia.auth.user.entity.AppUser;
import edu.concordia.auth.user.model.UserModel;
import edu.concordia.auth.user.services.PasswordResetService;
import edu.concordia.auth.user.services.UserService;
import edu.concordia.auth.util.JwtTokenUtil;
import edu.concordia.shared.model.response.GenericResponseEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Map;

import static edu.concordia.shared.entitiy.constants.Constants.API_VERSION;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("user-management")
@RequestMapping("/"+API_VERSION+"/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GenericResponseEntity<UserModel> getUserDetails() {

        var user = userService.loggedInUser();
        if(user == null)
            throw new UserNotFoundException("User not found");
        return GenericResponseEntity.success(user.toModel());
    }


}
