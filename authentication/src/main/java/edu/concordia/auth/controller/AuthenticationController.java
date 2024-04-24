package edu.concordia.auth.controller;

import edu.concordia.auth.exception.UserNotFoundException;
import edu.concordia.auth.model.request.LoginRequest;
import edu.concordia.auth.user.entity.AppUser;
import edu.concordia.auth.user.model.UserModel;
import edu.concordia.auth.user.services.PasswordResetService;
import edu.concordia.auth.user.services.UserService;
import edu.concordia.auth.util.JwtTokenUtil;
import edu.concordia.shared.error.AppException;
import edu.concordia.shared.model.response.GenericResponseEntity;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Map;

import static edu.concordia.shared.entitiy.constants.Constants.API_VERSION;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController("auth-management")
@RequestMapping("/"+API_VERSION+"/auth")
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GenericResponseEntity<Map<String,String>> login(@Valid @RequestBody LoginRequest contactModel) {

        logger.info("User log-in attempt {}",contactModel.getEmailAddress());
        var user = userService.loadUserByUsername(contactModel.getEmailAddress());
        if(user == null) {
            logger.debug("User not found attempt for mail id {}",contactModel.getEmailAddress());
            throw new UserNotFoundException("User not found");
        }try{
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(contactModel.getEmailAddress(), contactModel.getPassword()));
        } catch (Exception e) {
            logger.error("Error while login in {}",contactModel.getEmailAddress(),e);
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        var token = jwtTokenUtil.generateToken(contactModel.getEmailAddress());
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(contactModel.getEmailAddress(), contactModel.getPassword()));
        GenericResponseEntity<Map<String,String>> returnModel =
                GenericResponseEntity.success(Collections.singletonMap("token", token));
        return returnModel;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GenericResponseEntity<Map<String,String>> createContact(@Valid @RequestBody UserModel contactModel) {

        AppUser  user = userService.createUser(contactModel);
        GenericResponseEntity<Map<String,String>> returnModel =
                GenericResponseEntity.success(Collections.singletonMap("uuid", user.getUuid()));
        returnModel.add(linkTo(methodOn(AuthenticationController.class).createContact(contactModel)).withSelfRel());
        returnModel.add(linkTo(methodOn(AuthenticationController.class).login(null)).withRel("login"));
        returnModel.add(linkTo(methodOn(AuthenticationController.class)
                .initiatePasswordReset(contactModel.getEmailAddress())).withRel("initiate_reset_password"));
        return returnModel;
    }

    @GetMapping("/initiatepasswordreset")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GenericResponseEntity<Map<String,String>> initiatePasswordReset(@RequestParam("email") String email) {

        var user = userService.loadUserByUsername(email);
        if(user == null)
            throw new UserNotFoundException("User not found");
        var entry = passwordResetService.createPasswordResetEntry(user);
        GenericResponseEntity<Map<String,String>> returnModel =
                GenericResponseEntity.success(Collections.singletonMap("otp", entry.getToken()));
        returnModel.add(linkTo(methodOn(AuthenticationController.class).login(null)).withRel("login"));
        returnModel.add(linkTo(methodOn(AuthenticationController.class)
                .initiatePasswordReset(email)).withRel("initiate_reset_password"));
        return returnModel;
    }

    @PostMapping("/resetpassword")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GenericResponseEntity<Map<String,String>> createContact(@Valid @RequestBody LoginRequest contactModel) {

        var token = jwtTokenUtil.generateToken(contactModel.getEmailAddress());
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(contactModel.getEmailAddress(), contactModel.getPassword()));
        GenericResponseEntity<Map<String,String>> returnModel =
                GenericResponseEntity.success(Collections.singletonMap("token", token));
        return returnModel;
    }


}
