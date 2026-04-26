package org.otp.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.otp.DTO.OtpMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpPublisherImpl {
    private final RabbitTemplate rabbitTemplate;
    public void publishOtp(String phone, String otp){
        OtpMessage message = new OtpMessage(phone, otp, 0);
        rabbitTemplate.convertAndSend("otp.queue", message);
    }
}
