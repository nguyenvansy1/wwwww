package com.bookshopping.controller;

import com.bookshopping.model.AuthProvider;
import com.bookshopping.model.User;
import com.bookshopping.payload.request.LoginRequest;
import com.bookshopping.payload.request.RegisterRequest;
import com.bookshopping.payload.response.ResponseMessage;
import com.bookshopping.security.TokenProvider;
import com.bookshopping.service.UserService;
import com.bookshopping.util.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private OTPService otpService;

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());

        if (user == null) {
            return new ResponseEntity<>(new ResponseMessage("Email chưa được đăng ký trong hệ thống"), HttpStatus.NOT_FOUND);
        }
        if(user.getPassword() == null) {
            return new ResponseEntity<>(new ResponseMessage("Email đã được đăng ký trong hệ thống"), HttpStatus.NOT_FOUND);
        }
        else if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("Email and password valid");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            System.out.println("Set authentication");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.createToken(authentication);
            return new ResponseEntity<>(new ResponseMessage(token), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage("Mật khẩu không hợp lệ."), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@RequestBody RegisterRequest register) {
        if (userService.findByEmail(register.getEmail()) != null) {
            return new ResponseEntity<>(new ResponseMessage("Email đã được đăng ký trước đó !!!"), HttpStatus.BAD_REQUEST);
        }
        if(otpService.getOtp(register.getEmail()) == register.getOtp()) {
            if(register.getNewPass().equals(register.getConfirmPass())) {
                User user = new User();
                user.setName(register.getName());
                user.setEmail(register.getEmail());
                user.setPassword(passwordEncoder.encode(register.getNewPass()));
                user.setProvider(AuthProvider.local);

                otpService.clearOTP(register.getEmail());
                userService.saveCreateRelationship(user);
                return new ResponseEntity<>(new ResponseMessage("Đăng ký thành công !!!"), HttpStatus.OK);
            }
            else return new ResponseEntity<>(new ResponseMessage("Mật khẩu không trùng khớp !!!"), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ResponseMessage("OTP không hợp lệ !!!"), HttpStatus.BAD_REQUEST);
        }
    }
}
