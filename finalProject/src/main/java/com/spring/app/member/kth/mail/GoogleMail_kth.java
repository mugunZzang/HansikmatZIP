package com.spring.app.member.kth.mail;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class GoogleMail_kth {

    // 인증메일 보내기
    public void sendMail(String email, String certificationCode) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("rhinoceros445@gmail.com");
        mailSender.setPassword("bajk cell jgqb pxoo");
        mailSender.setDefaultEncoding("UTF-8");

        Properties prop = mailSender.getJavaMailProperties();

        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.starttls.required", "true");
        prop.put("mail.smtp.connectiontimeout", "5000");
        prop.put("mail.smtp.timeout", "5000");
        prop.put("mail.smtp.writetimeout", "5000");

        // 메일 내용 작성
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailSender.getUsername());
        message.setTo(email);
        message.setSubject("[한식맛ZIP] 회원가입 이메일 인증");
        message.setText("인증번호는 " + certificationCode + " 입니다.\n"
                      + "5분 이내에 인증번호를 입력해주세요.");

        // 메일 발송
        mailSender.send(message);

    }

}