package com.ensa.ENSAPAY.services;

import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl{

    @Autowired
    public EmailService emailService;

    @SneakyThrows
    public void sendEmailWithTemplating(String emailReceiver,String username, String password,String firstName,String lastName,String loginLink){
        System.out.println(emailReceiver);
        final Email email = DefaultEmail.builder()
                .from(new InternetAddress("ensapay5566@gmail.com", "Ensa Pay"))
                .to(Lists.newArrayList(new InternetAddress(emailReceiver)))
                .subject("Your ENSAPAY account has been created successfully")
                .body("")
                .encoding("UTF-8").build();

        final Map<String, Object> modelObject = new HashMap<>();
        modelObject.put("loginLink",loginLink);
        modelObject.put("username",username);
        modelObject.put("password",password);
        modelObject.put("firstName",firstName);
        modelObject.put("lastName",lastName);

        emailService.send(email, "emailTemplate.ftl", modelObject);
    }
}
