package com.example.Gestion_des_evaluations.Entity.Auth.Service;

public interface OtpSenderService {
    void sendOtp(String phoneNumber, String otp);
}