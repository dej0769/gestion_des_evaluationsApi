package com.example.Gestion_des_evaluations.Service.Mail;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DevEmailService implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String text) {
        System.out.println("DEV EMAIL => to=" + to + ", subject=" + subject + ", text=" + text);
    }

    @Override
    public void sendResetPasswordEmail(String toEmail, String resetLink) {
        System.out.println("DEV RESET LINK => " + resetLink);
    }

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        System.out.println("DEV OTP => to=" + toEmail + ", otp=" + otp);
    }
}