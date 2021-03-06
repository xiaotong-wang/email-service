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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
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
        // ?????????
        mimeMessageHelper.setFrom(from);
        // ????????????
        mimeMessageHelper.setSubject(mailDTO.getSubject());
        // ?????????
        mimeMessageHelper.setTo(mailDTO.getTo());
        // ??????????????????
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(mailDTO.getModelName()+".ftl");
        // ????????????
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template,mailDTO.getModelContent());
        // ????????????
        if (null != mailDTO.getAttachmentUrl()){
            for (String s : mailDTO.getAttachmentUrl()) {
                mimeMessageHelper.addAttachment(s.substring(s.indexOf("-")+1), new File(s));
            }
        }
        mimeMessageHelper.setText(text,true);
        // ????????????
        javaMailSender.send(mimeMessage);
    }

    @Async("emailTaskExecutor")
    @Override
    public void sendEmailWithContent(MailDTO mailDTO) {
        try {
            sendEmailWithContentMethod(mailDTO);
            mailDTO.setSendNum(1).setSendStatus(MailStatus.SUCCESS);
        } catch (Exception e) {
            System.out.println(e);
            mailDTO.setSendNum(1).setSendStatus(MailStatus.FAIL).setErrorMsg(e.getMessage());
        }
        mailDTOList.add(mailDTO);
    }
    @Override
    public void sendEmailWithContentMethod(MailDTO mailDTO) throws MessagingException{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        // ?????????
        mimeMessageHelper.setFrom(from);
        // ????????????
        mimeMessageHelper.setSubject(mailDTO.getSubject());
        // ?????????
        mimeMessageHelper.setTo(mailDTO.getTo());
        // ????????????
        mimeMessageHelper.setText(mailDTO.getContent());
        // ????????????
        if (null != mailDTO.getAttachmentUrl()){
            for (String s : mailDTO.getAttachmentUrl()) {
                mimeMessageHelper.addAttachment(s.substring(s.indexOf("-")+1), new File(s));
            }
        }
        // ????????????
        javaMailSender.send(mimeMessage);
    }
}
