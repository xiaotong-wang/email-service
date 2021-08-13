package com.otd.emaildemo.service.impl;

import com.otd.emaildemo.constant.MailStatus;
import com.otd.emaildemo.dto.MailDTO;
import com.otd.emaildemo.service.MailService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MailServiceImpl
 * @Description:
 * @Author: xiaotong.wang
 * @Date: 2021/8/12 17:22
 */
@Service
public class MailServiceImpl implements MailService {

    public static List<MailDTO> mailDTOList  = new ArrayList<>();
    private JavaMailSender javaMailSender;
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${spring.mail.username}")
    private String from;

    public MailServiceImpl(JavaMailSender javaMailSender,FreeMarkerConfigurer freeMarkerConfigurer) {
        this.javaMailSender = javaMailSender;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailWithModel(MailDTO mailDTO){

        try {
            sendEmailWithModelMethod(mailDTO);
            mailDTO.setSendNum(1).setSendStatus(MailStatus.SUCCESS);
        } catch (Exception e) {
            mailDTO.setSendNum(1).setSendStatus(MailStatus.FAIL).setErrorMsg(e.getMessage());
        }
        mailDTOList.add(mailDTO);
    }
    @Override
    public void sendEmailWithModelMethod(MailDTO mailDTO) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        // 发件人
        mimeMessageHelper.setFrom(from);
        // 邮件标题
        mimeMessageHelper.setSubject(mailDTO.getSubject());
        // 收件人
        mimeMessageHelper.setTo(mailDTO.getTo());
        // 获取邮件模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(mailDTO.getModelName()+".ftl");
        // 模板填充
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template,mailDTO.getModelContent());
        mimeMessageHelper.setText(text,true);
        // 邮件发送
        javaMailSender.send(mimeMessage);
    }

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailWithContent(MailDTO mailDTO) {
        try {
            sendEmailWithContentMethod(mailDTO);
            mailDTO.setSendNum(1).setSendStatus(MailStatus.SUCCESS);
        } catch (Exception e) {
            mailDTO.setSendNum(1).setSendStatus(MailStatus.FAIL).setErrorMsg(e.getMessage());
        }
        mailDTOList.add(mailDTO);
    }
    @Override
    public void sendEmailWithContentMethod(MailDTO mailDTO) throws MessagingException{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        // 发件人
        mimeMessageHelper.setFrom(from);
        // 邮件标题
        mimeMessageHelper.setSubject(mailDTO.getSubject());
        // 收件人
        mimeMessageHelper.setTo(mailDTO.getTo());
        // 邮件内容
        mimeMessageHelper.setText(mailDTO.getContent());
        // 邮件发送
        javaMailSender.send(mimeMessage);
    }
}
