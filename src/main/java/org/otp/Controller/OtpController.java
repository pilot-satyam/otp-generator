package org.otp.Controller;

import lombok.RequiredArgsConstructor;
import org.otp.Service.OtpService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;

    @PostMapping("/send")
    public String sendOtp(@RequestParam String phoneNumber){
        return otpService.sendOtp(phoneNumber);
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String phoneNumber, @RequestParam String otp){
        boolean result = otpService.verifyOtp(phoneNumber, otp);
        return  result ? "SUCCESS" : "FAIL";
    }

}
