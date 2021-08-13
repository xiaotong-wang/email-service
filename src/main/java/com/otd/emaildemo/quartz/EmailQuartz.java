package com.otd.emaildemo.quartz;

import com.otd.emaildemo.constant.MailStatus;
import com.otd.emaildemo.constant.MailType;
import com.otd.emaildemo.dto.MailDTO;
import com.otd.emaildemo.service.MailService;
import com.otd.emaildemo.service.impl.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: EmailQuartz
 * @Description:
 * @Author: xiaotong.wang
 * @Date: 2021/8/13 15:34
 */
@Component
public class EmailQuartz {
    @Autowired
    MailService mailService;

    @Scheduled(cron = "*/15 * * * * ?")
    public void sendRetry(){
        List<MailDTO> mailDTOList = MailServiceImpl.mailDTOList;
        for (MailDTO mailDTO : mailDTOList) {
            if (MailStatus.FAIL.equals(mailDTO.getSendStatus()) && mailDTO.getSendNum()<5){
                try {
                    // 模板邮件
                    if (MailType.MAIL_WITH_MODEL.equals(mailDTO.getMailType())) {
                        mailService.sendEmailWithModelMethod(mailDTO);
                    }
                    // 文本邮件
                    if (MailType.MAIL_WITH_CONTENT.equals(mailDTO.getMailType())) {
                        mailService.sendEmailWithContentMethod(mailDTO);
                    }
                    // 发送成功，次数+1，并修改状态
                    mailDTO.setSendNum(mailDTO.getSendNum() + 1);
                    mailDTO.setSendStatus(MailStatus.SUCCESS);
                } catch (Exception e) {
                    // 发送失败，次数+1，并记录失败原因
                    mailDTO.setSendNum(mailDTO.getSendNum()+1);
                    mailDTO.setErrorMsg(e.getMessage());
                    // 失败5次，更改邮件状态
                    if (5==mailDTO.getSendNum()) {
                        mailDTO.setSendStatus(MailStatus.FAIL_FINAL);
                    }
                }
            }
        }
    }
}
