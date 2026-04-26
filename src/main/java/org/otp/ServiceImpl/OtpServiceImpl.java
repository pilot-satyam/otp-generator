package org.otp.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.otp.Service.OtpService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final StringRedisTemplate redisTemplate;
    private final OtpPublisherImpl otpPublisher;
    private static final int TTL_seconds = 30;

    @Override
    public String sendOtp(String phoneNumber) {
        String key = "otp:" + phoneNumber;
        //checking idempotency
        String existingOtp = redisTemplate.opsForValue().get(key);
        if(existingOtp != null){
            return existingOtp;
        }
        String otp = generateOtp();
        redisTemplate.opsForValue().set(key, otp, TTL_seconds, TimeUnit.SECONDS);
        // 🔔 simulate SMS send
//        System.out.println("Sending OTP to " + phoneNumber + " : " + otp);
        otpPublisher.publishOtp(phoneNumber, otp); //sending to Queue
        return otp;
    }

    @Override
    public boolean verifyOtp(String phoneNumber, String otp) {
        String key = "otp:" + phoneNumber;
        String storedOtp = redisTemplate.opsForValue().get(key);
        if(storedOtp != null && storedOtp.equals(otp)){
            //invalidate after use
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    private String generateOtp(){
        int otp = new Random().nextInt(900000) + 100000;
        return String.valueOf(otp);
    }
}
