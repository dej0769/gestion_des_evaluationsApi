package com.example.Gestion_des_evaluations.Service.Mail;



public interface EmailService {
    void sendEmail(String to, String subject, String text);
    void sendResetPasswordEmail(String toEmail, String resetLink);
    void sendOtpEmail(String toEmail, String otp);
}