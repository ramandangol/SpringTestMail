package com.email.springmail.mail;

import com.email.springmail.webDTO.MailObject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailUtil {

    @Autowired
    @Qualifier("javaMailSender")
    public JavaMailSender emailSender;

    @Autowired
    private SimpleMailMessage template;

    @Autowired
    private Configuration freemarkerConfig;

    public void sendSimpleMessage(String recipient, String emailSubject,
                                  String emailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(emailSubject);
        message.setText(emailBody);
//        message.setFrom(DemoConstants.EMAIL_FROM);
        emailSender.send(message);
    }


    public void sendMessageWithTemplate(String recipient,
                                        String emailSubject,
                                        SimpleMailMessage template,
                                        String templateArgs){
        String text = String.format(template.getText(),templateArgs);
        sendSimpleMessage(recipient,emailSubject,text);
    }

    public void sendMessageWithAttachment(String recipient, String emailSubject,
                                          String emailBody, String pathToAttachment)
            throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipient);
        helper.setSubject(emailSubject);
        helper.setText(emailBody);

        FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Invoice", file);

        emailSender.send(message);
    }

    public void sendHtmlMail()throws Exception{
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        Map<String, Object> model = new HashMap();
        model.put("user", "raman");
        model.put("date", LocalDate.now().toString());

        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
        Template t = freemarkerConfig.getTemplate("mailTemplate.ftl");
        String text1 = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

        helper.setTo("ramandangol07@gmail.com");
        helper.setText(text1,true);
        helper.setSubject("hello test");

        emailSender.send(message);
    }

}
