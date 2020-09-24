package com.example.springbootsecurityjwt.Controller;

import com.example.springbootsecurityjwt.Model.AuthenticationRequest;
import com.example.springbootsecurityjwt.Model.AuthenticationResponse;
import com.example.springbootsecurityjwt.Service.MyUserDetailsService;
import com.example.springbootsecurityjwt.jwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private jwtUtil jwtTokenUtil;

    @RequestMapping("/hello")
    public String Hello(){
        return "Hello";
    }

    @RequestMapping(value = "/authenticate" ,method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

       try {
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
           );
       }
       catch (BadCredentialsException e) {
           throw new Exception("Incorect username or password",e);
       }

       final UserDetails userDetails = userDetailsService
               .loadUserByUsername(authenticationRequest.getUsername());

       final String jwt = jwtTokenUtil.generateToken(userDetails);

       return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }
}
