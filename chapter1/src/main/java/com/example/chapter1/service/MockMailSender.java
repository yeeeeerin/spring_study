package com.example.chapter1.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.List;

/*
메일 전송 확인용 클래스
 */
public class MockMailSender implements MailSender {

    private List<String> requests = new ArrayList<String>();

    public List<String> getRequests(){
        return requests;
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        requests.add(simpleMessage.getTo()[0]);
    }

    @Override
    public void send(SimpleMailMessage[] simpleMessages) throws MailException {

    }
}
