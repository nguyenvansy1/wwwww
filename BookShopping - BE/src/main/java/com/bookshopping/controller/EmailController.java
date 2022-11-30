package com.bookshopping.controller;

import com.bookshopping.model.User;
import com.bookshopping.payload.response.ResponseMessage;
import com.bookshopping.service.UserService;
import com.bookshopping.util.EmailService;
import com.bookshopping.util.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private OTPService otpService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @GetMapping("/getOtpRegister")
    public ResponseEntity<ResponseMessage> getOtpRegister(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            int otp = otpService.generateOTP(email);
            String message = "<h3>Cảm ơn bạn đã sử dụng ứng dụng của chúng tôi !!!</h3>"
                    + "<p>Mã OTP để đăng ký tài khoản của bạn là: " + otp + ".</p>"
                    + "<p>Mã OTP có hiệu lực trong thời gian 4 phút.</p>";
            if(emailService.sendEmail(email, "Tin Tức 24h", message))
                return new ResponseEntity<>(new ResponseMessage(String.valueOf(otp)), HttpStatus.OK);
            return new ResponseEntity<>(new ResponseMessage("Gửi email bị lỗi"), HttpStatus.BAD_REQUEST);
        }
        else return new ResponseEntity<>(new ResponseMessage("Email đã tồn tại trong hệ thống"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getOtp")
    public ResponseEntity<ResponseMessage> getOtp(@RequestParam String email) {
        int otp = otpService.generateOTP(email);
        String message = "<h3>Cảm ơn bạn đã sử dụng ứng dụng của chúng tôi !!!</h3>"
                + "<p>Mã OTP để thay đổi mật khẩu của bạn là: " + otp + ".</p>"
                + "<p>Mã OTP có hiệu lực trong thời gian 4 phút.</p>";
        if(emailService.sendEmail(email, "DT BookStore", message))
            return new ResponseEntity<>(new ResponseMessage(String.valueOf(otp)), HttpStatus.OK);
        return new ResponseEntity<>(new ResponseMessage("Gửi email bị lỗi"), HttpStatus.BAD_REQUEST);
    }
}
