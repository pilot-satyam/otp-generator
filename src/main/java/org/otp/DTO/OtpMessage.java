package org.otp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpMessage {
    private String phone;
    private String otp;
    private int retryCount;
}
