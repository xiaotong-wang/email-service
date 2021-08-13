package com.otd.emaildemo.controller;

import com.otd.emaildemo.dto.Mail;
import com.otd.emaildemo.dto.MailDTO;
import com.otd.emaildemo.service.MailService;
import com.otd.emaildemo.utils.MailUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/send-content")
    public ResponseEntity<Boolean> sendWithContent(@RequestBody Mail mail) throws Exception {
        // 参数校验
        MailDTO mailDTO = MailUtils.mailWithContentCheck(mail);
        // 异步调用
        mailService.sendEmailWithContent(mailDTO);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PostMapping("/send-templete")
    public ResponseEntity<Boolean> sendWithModel(@RequestBody Mail mail) throws Exception {

        // 参数校验
        MailDTO mailDTO = MailUtils.mailWithModelCheck(mail);
        // 异步调用
        mailService.sendEmailWithModel(mailDTO);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }


}
