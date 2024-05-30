package org.appointmentschedulingsystem.controller;

import org.appointmentschedulingsystem.dtos.JwtResponse;
import org.appointmentschedulingsystem.dtos.JwtRequest;
import org.appointmentschedulingsystem.security.authentication.JwtHelper;
import org.appointmentschedulingsystem.dtos.UserDto;
import org.appointmentschedulingsystem.repositories.UserRepository;
import org.appointmentschedulingsystem.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

//ToDo: Use final references for dependencies
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager manager;
    private final JwtHelper helper;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(
            UserDetailsService userDetailsService,
            AuthenticationManager manager,
            JwtHelper helper,
            UserService userService,
            UserRepository userRepository
    ) {
        this.userDetailsService = userDetailsService;
        this.manager = manager;
        this.helper = helper;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

        doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .JwtToken(token)
                .userName(userDetails.getUsername())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    @PostMapping("/userAdd")
    public ResponseEntity<UserDto> userAdd(@RequestBody UserDto userDTO) {
        UserDto userDto1 = userService.addUser(userDTO);
        return ResponseEntity.ok(userDto1);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

}