package com.bookshopping.controller;

import com.bookshopping.model.User;
import com.bookshopping.payload.request.UserRequest;
import com.bookshopping.payload.response.ResponseMessage;
import com.bookshopping.repository.UserRepository;
import com.bookshopping.security.TokenProvider;
import com.bookshopping.service.UserService;
import com.bookshopping.util.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private OTPService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getInfoFromToken")
    public ResponseEntity<User> getInfoFormToken(@RequestParam String token)  {
        Integer id = tokenProvider.getUserIdFromToken(token);
        User user = userService.findById(id);

        if(user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<User> findById(@RequestParam Integer id)  {
        User user = userService.findById(id);

        if(user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> userList()  {
        List<User> user = userRepository.userList();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/updateEmail")
    public ResponseEntity<ResponseMessage> updateEmail(@RequestParam @NotBlank Integer id,
                                                       @RequestParam @Email String email,
                                                       @RequestParam int otp) {
        User user = userService.findById(id);
        if(user.getEmail().equals(email)) {
            return new ResponseEntity<>(new ResponseMessage("Email trùng với email cũ"), HttpStatus.BAD_REQUEST);
        } else if(userService.existByEmailOtherUser(id, email)) {
            return new ResponseEntity<>(new ResponseMessage("Email đã được liên kết bằng tài khoản khác"), HttpStatus.BAD_REQUEST);
        } else if(otpService.getOtp(email) != otp) {
            return new ResponseEntity<>(new ResponseMessage("Mã otp không hợp lệ hoặc đã hết hạn."), HttpStatus.BAD_REQUEST);
        }
        if(userService.updateEmail(id, email) == 1)
            return new ResponseEntity<>(new ResponseMessage("Thay đổi email thành công !!!"), HttpStatus.OK);
        return new ResponseEntity<>(new ResponseMessage("Thay đổi email thất bại."), HttpStatus.OK);
    }

    @PostMapping("/updateInfo")
    public ResponseEntity<ResponseMessage> updateInfo(@RequestParam Integer id, @RequestBody User user) {
        User userDb = userService.findById(id);
        if(userDb == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        System.out.println(user.getGender());
        int record = userService.updateInfo(id, user);
        if(record > 0) {
            return new ResponseEntity<>(new ResponseMessage("Cập nhật thành công !!!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("Cập nhật thất bại !!!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<ResponseMessage> updatePassword(@RequestParam Integer id,
                                                          @RequestParam String oldPass,
                                                          @RequestParam String newPass,
                                                          @RequestParam int otp) {
        User user = userService.findById(id);

        if(user == null) {
            return new ResponseEntity<>(new ResponseMessage("Không tồn tại người dùng trong hệ thống !!!"), HttpStatus.NO_CONTENT);
        }
        // If old password = password in database
        if(otpService.getOtp(user.getEmail()) != otp) {
            return new ResponseEntity<>(new ResponseMessage("Mã OTP không đúng hoặc hết hiệu lực !!!"), HttpStatus.BAD_REQUEST);
        }
        else if(!passwordEncoder.matches(oldPass, user.getPassword())) {
            return new ResponseEntity<>(new ResponseMessage("Mật khẩu cũ không đúng !!!"), HttpStatus.BAD_REQUEST);
        }
        else if(passwordEncoder.matches(newPass, user.getPassword())){
            return new ResponseEntity<>(new ResponseMessage("Mật khẩu mới trùng mật khẩu cũ !!!"), HttpStatus.BAD_REQUEST);
        }
        otpService.clearOTP(user.getEmail());
        userService.updatePassword(id, passwordEncoder.encode(newPass));
        return new ResponseEntity<>(new ResponseMessage("Cập nhật mật khẩu thành công !!!"), HttpStatus.OK);
    }
}
