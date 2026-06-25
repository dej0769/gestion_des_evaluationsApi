package com.example.Gestion_des_evaluations.Entity.Auth.Service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class MockOtpSenderService implements OtpSenderService {

    @Override
    public void sendOtp(String phoneNumber, String otp) {
        System.out.println("[DEV] OTP pour " + phoneNumber + " = " + otp);
    }
}
