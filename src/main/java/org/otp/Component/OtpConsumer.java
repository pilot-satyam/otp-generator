package org.otp.Component;

import org.otp.DTO.OtpMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpConsumer {
    private final RabbitTemplate rabbitTemplate;

    public OtpConsumer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "otp.queue")
    public void consume(OtpMessage message) {
        try{
            simulateSmsSend(message);
        }
        catch (Exception ex){
            int retry = message.getRetryCount();
            if(retry >= 3){
                System.out.println("Sending it to DLQ : " + message);
                rabbitTemplate.convertAndSend("otp.dlq", message);
            }else{
                message.setRetryCount(message.getRetryCount()+1);
                rabbitTemplate.convertAndSend("otp.retry", message);
            }
        }
    }

    private void simulateSmsSend(OtpMessage message) {
        if(new Random().nextInt(10) < 9){
            throw new RuntimeException("SMS failed!! ");
        }
        System.out.println("SMS sent to ..." + message.getPhone() + " With otp :  " + message.getOtp());
    }
}
