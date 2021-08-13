package com.otd.emaildemo.utils;

import com.otd.emaildemo.constant.MailType;
import com.otd.emaildemo.dto.Mail;
import com.otd.emaildemo.dto.MailDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * @ClassName: MailUtils
 * @Description:
 * @Author: xiaotong.wang
 * @Date: 2021/8/13 13:46
 */
public class MailUtils {
    public static MailDTO mailWithContentCheck(Mail mail) throws Exception {
        MailDTO mailDTO = new MailDTO();
        String[] tempArray = mail.getTo().split(";");
        // step1: 定义一个list列表，并循环赋值
        ArrayList<String> strList = new ArrayList<>();
        for (String s : tempArray) {
            //去除空的
            if (StringUtils.isBlank(s)) {
                continue;
            } else {
                strList.add(s);
            }
        }
        if(strList.size() < 1){
            //收件人为空，则返回
            throw new Exception("收件人为空异常");
        }
        // step2: 把list列表转换给一个新定义的中间数组，并赋值给它
        String[] strArrLast = strList.toArray(new String [strList.size()]);
        return mailDTO.setTo(strArrLast)
                .setMailType(MailType.MAIL_WITH_CONTENT)
                .setSubject(mail.getSubject())
                .setContent(mail.getContent());
    }
    public static MailDTO mailWithModelCheck(Mail mail) throws Exception {
        MailDTO mailDTO = new MailDTO();
        String[] tempArray = mail.getTo().split(";");
        // step1: 定义一个list列表，并循环赋值
        ArrayList<String> strList = new ArrayList<>();
        for (String s : tempArray) {
            //去除空的
            if (StringUtils.isBlank(s)) {
                continue;
            } else {
                strList.add(s);
            }
        }
        if(strList.size() < 1){
            //收件人为空，则返回
            throw new Exception("收件人为空异常");
        }
        // step2: 把list列表转换给一个新定义的中间数组，并赋值给它
        String[] strArrLast = strList.toArray(new String [strList.size()]);
        return mailDTO.setTo(strArrLast)
                .setSubject(mail.getSubject())
                .setMailType(MailType.MAIL_WITH_MODEL)
                .setModelName(mail.getModelName())
                .setModelContent(mail.getModelContent());
    }
}
