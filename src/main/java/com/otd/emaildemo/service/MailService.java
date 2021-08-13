package com.otd.emaildemo.service;

import com.otd.emaildemo.dto.MailDTO;
import freemarker.template.TemplateException;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName: MailService
 * @Description:
 * @Author: xiaotong.wang
 * @Date: 2021/8/12 17:20
 */
public interface MailService {
    /**
     * 使用模板发送邮件
     * @param mailDTO
     * @throws MessagingException
     * @throws IOException
     * @throws TemplateException
     */
    void sendEmailWithModel(MailDTO mailDTO) throws MessagingException, IOException, TemplateException;

    /**
     * 使用模板发送邮件内部方法
     * @param mailDTO
     * @throws MessagingException
     * @throws IOException
     * @throws TemplateException
     */
    void sendEmailWithModelMethod(MailDTO mailDTO) throws MessagingException, IOException, TemplateException;

    /**
     * 文本形式发送邮件
     * @param mailDTO
     * @throws MessagingException
     */
    void sendEmailWithContent(MailDTO mailDTO) throws MessagingException;
    /**
     * 文本形式发送邮件内部方法
     * @param mailDTO
     * @throws MessagingException
     */
    void sendEmailWithContentMethod(MailDTO mailDTO) throws MessagingException;
}
