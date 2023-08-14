package com.jial;

import com.jial.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail(){
        mailClient.sendMail("1165552248@qq.com","我是你爸爸","Welcome");
    }
    @Test
    public void sendHtmlMail(){
        Context context = new Context();
        context.setVariable("username","test");

        String process = templateEngine.process("/mail/demo", context);
        System.out.println(process);
        mailClient.sendMail("2645118890@qq.com","HTML",process);
    }
}
