package com.email.springmail.controllers;

import com.email.springmail.mail.EmailUtil;
import com.email.springmail.webDTO.MailObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;


@RestController
public class HomeController {


    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private SimpleMailMessage template;

    @RequestMapping(method = RequestMethod.GET)
    public String showHomePage() {
        return "home";
    }

    @PostMapping(value = "/send")
    public void sendMail(@RequestBody MailObject mailObject){
        mailObject.setSubject("Test mail");
        mailObject.setText("Hello ");
        emailUtil.sendSimpleMessage("ramandangol07@gmail.com",mailObject.getSubject(),mailObject.getText());
    }

    @GetMapping(value = "/send1")
    public void sendMailWithTemplatehtml()throws Exception{
        emailUtil.sendHtmlMail();
    }

}
