package com.example.Gestion_des_evaluations.Entity.Auth.Service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {
    public void sendSms(String phoneNumber, String message) {
        System.out.println("SMS to " + phoneNumber + " : " + message);
    }
}

