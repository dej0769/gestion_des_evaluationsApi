package com.example.Gestion_des_evaluations.Entity.Auth.Service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class SmsOtpSenderService implements OtpSenderService {

    @Override
    public void sendOtp(String phoneNumber, String otp) {
        // Appel vers Twilio, Spring SMS API, etc.
        System.out.println("Envoi SMS vers " + phoneNumber + " : " + otp);
    }
}