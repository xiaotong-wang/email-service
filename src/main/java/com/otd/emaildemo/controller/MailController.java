package com.otd.emaildemo.controller;

import com.otd.emaildemo.dto.Mail;
import com.otd.emaildemo.dto.MailDTO;
import com.otd.emaildemo.service.MailService;
import com.otd.emaildemo.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;

/**
 * @ClassName: MailController
 * @Description:
 * @Author: xiaotong.wang
 * @Date: 2021/8/11 11:15
 */
@RestController
public class MailController {
    private MailService mailService;
    public MailController(MailService mailService) {
        this.mailService=mailService;
    }
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @PostMapping("/send-test")
    public ResponseEntity<Boolean> sendTest(@RequestParam("file") MultipartFile file) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        // 发件人
        mimeMessageHelper.setFrom("459236903@qq.com");
        // 邮件标题
        mimeMessageHelper.setSubject("ceshi");
        // 收件人
        mimeMessageHelper.setTo("459236903@qq.com");
        // 邮件内容
        mimeMessageHelper.setText("ceshi");
        // 添加附件
        mimeMessageHelper.addAttachment("123.txt",file);
        // 邮件发送
        javaMailSender.send(mimeMessage);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PostMapping("/send-content")
    public ResponseEntity<Boolean> sendWithContent(Mail mail) throws Exception {
        System.out.println(mail);
        // 参数校验
        MailDTO mailDTO = MailUtils.mailWithContentCheck(mail);
        // 异步调用
        mailService.sendEmailWithContent(mailDTO);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PostMapping("/send-templete")
    public ResponseEntity<Boolean> sendWithModel(Mail mail) throws Exception {
        System.out.println(mail);
        // 参数校验
        MailDTO mailDTO = MailUtils.mailWithModelCheck(mail);
        // 异步调用
        mailService.sendEmailWithModel(mailDTO);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }


}
