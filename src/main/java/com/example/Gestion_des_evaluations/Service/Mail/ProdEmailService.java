package com.example.Gestion_des_evaluations.Service.Mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class ProdEmailService implements EmailService {

    private final JavaMailSender mailSender;

    public ProdEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@ton-domaine.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendResetPasswordEmail(String toEmail, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@ton-domaine.com");
            helper.setTo(toEmail);
            helper.setSubject("Réinitialisation de mot de passe");
            helper.setText("""
                    <p>Bonjour,</p>
                    <p>Vous avez demandé la réinitialisation de votre mot de passe.</p>
                    <p>Cliquez sur le lien ci-dessous pour le modifier :</p>
                    <p><a href="%s">Réinitialiser mon mot de passe</a></p>
                    <p>Si vous n'êtes pas à l'origine de cette demande, ignorez cet email.</p>
                    """.formatted(resetLink), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }

        @Override
        public void sendOtpEmail(String toEmail, String otp) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@ton-domaine.com");
            message.setTo(toEmail);
            message.setSubject("Votre code OTP");
            message.setText("Votre code OTP est : " + otp + ". Il expire dans 5 minutes.");
            mailSender.send(message);
        }
    }
