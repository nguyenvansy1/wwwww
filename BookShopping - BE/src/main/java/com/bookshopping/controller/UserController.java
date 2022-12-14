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
            return new ResponseEntity<>(new ResponseMessage("Email tr??ng v???i email c??"), HttpStatus.BAD_REQUEST);
        } else if(userService.existByEmailOtherUser(id, email)) {
            return new ResponseEntity<>(new ResponseMessage("Email ???? ???????c li??n k???t b???ng t??i kho???n kh??c"), HttpStatus.BAD_REQUEST);
        } else if(otpService.getOtp(email) != otp) {
            return new ResponseEntity<>(new ResponseMessage("M?? otp kh??ng h???p l??? ho???c ???? h???t h???n."), HttpStatus.BAD_REQUEST);
        }
        if(userService.updateEmail(id, email) == 1)
            return new ResponseEntity<>(new ResponseMessage("Thay ?????i email th??nh c??ng !!!"), HttpStatus.OK);
        return new ResponseEntity<>(new ResponseMessage("Thay ?????i email th???t b???i."), HttpStatus.OK);
    }

    @PostMapping("/updateInfo")
    public ResponseEntity<ResponseMessage> updateInfo(@RequestParam Integer id, @RequestBody User user) {
        User userDb = userService.findById(id);
        if(userDb == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        System.out.println(user.getGender());
        int record = userService.updateInfo(id, user);
        if(record > 0) {
            return new ResponseEntity<>(new ResponseMessage("C???p nh???t th??nh c??ng !!!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("C???p nh???t th???t b???i !!!"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<ResponseMessage> updatePassword(@RequestParam Integer id,
                                                          @RequestParam String oldPass,
                                                          @RequestParam String newPass,
                                                          @RequestParam int otp) {
        User user = userService.findById(id);

        if(user == null) {
            return new ResponseEntity<>(new ResponseMessage("Kh??ng t???n t???i ng?????i d??ng trong h??? th???ng !!!"), HttpStatus.NO_CONTENT);
        }
        // If old password = password in database
        if(otpService.getOtp(user.getEmail()) != otp) {
            return new ResponseEntity<>(new ResponseMessage("M?? OTP kh??ng ????ng ho???c h???t hi???u l???c !!!"), HttpStatus.BAD_REQUEST);
        }
        else if(!passwordEncoder.matches(oldPass, user.getPassword())) {
            return new ResponseEntity<>(new ResponseMessage("M???t kh???u c?? kh??ng ????ng !!!"), HttpStatus.BAD_REQUEST);
        }
        else if(passwordEncoder.matches(newPass, user.getPassword())){
            return new ResponseEntity<>(new ResponseMessage("M???t kh???u m???i tr??ng m???t kh???u c?? !!!"), HttpStatus.BAD_REQUEST);
        }
        otpService.clearOTP(user.getEmail());
        userService.updatePassword(id, passwordEncoder.encode(newPass));
        return new ResponseEntity<>(new ResponseMessage("C???p nh???t m???t kh???u th??nh c??ng !!!"), HttpStatus.OK);
    }
}
