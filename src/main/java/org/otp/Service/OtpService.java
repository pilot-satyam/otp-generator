package org.otp.Service;

public interface OtpService {
    String sendOtp(String phoneNumber);
    boolean verifyOtp(String phoneNumber, String otp);
}
