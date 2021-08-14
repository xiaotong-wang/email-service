package com.otd.emaildemo.utils;

import com.alibaba.fastjson.JSON;
import com.otd.emaildemo.constant.MailAttachment;
import com.otd.emaildemo.constant.MailType;
import com.otd.emaildemo.dto.Mail;
import com.otd.emaildemo.dto.MailDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * 邮件工具类
 * @ClassName: MailUtils
 * @Description:
 * @Author: xiaotong.wang
 * @Date: 2021/8/13 13:46
 */
public class MailUtils {
    private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("application");
    /**
     * 转换坐标所需要的配置信息
     */
    private final static String FILE_PATH = RESOURCE_BUNDLE.getString("mail.upload.url");

    /**
     * 文本邮件检查
     * @param: mail
     * @return:
     * @throws: Exception
     */
    public static MailDTO mailWithContentCheck(Mail mail) throws Exception {
        // 通用检查方法
        MailDTO mailDTO = mailCheck(mail);
        // 设置文本内容
        return mailDTO.setSubject(mail.getSubject())
                .setMailType(MailType.MAIL_WITH_CONTENT)
                .setContent(mail.getContent());
    }

    /**
     *
     * @param: mail
     * @return: MailDTO
     * @throws: Exception
     */
    public static MailDTO mailWithModelCheck(Mail mail) throws Exception {
        // 通用检查方法
        MailDTO mailDTO = mailCheck(mail);
        // 设置模板
        return mailDTO.setSubject(mail.getSubject())
                .setMailType(MailType.MAIL_WITH_MODEL)
                .setModelName(mail.getModelName())
                .setModelContent(JSON.parseObject(mail.getModelContent(), Map.class));
    }

    /**
     * 通用检查方法
     * @param: mail
     * @return:
     * @throws: Exception
     */
    public static MailDTO mailCheck(Mail mail) throws Exception {
        MailDTO mailDTO = new MailDTO();
        // 生成邮件编号
        String mailNo = UUID.randomUUID().toString().replace("-", "");
        // 接收人检查
        String[] to = receiverCheck(mail.getTo());
        // 附件检查
        if (null != mail.getAttachment()) {
            String[] attachmentUrl = attachmentCheck(mailNo, mail.getAttachment());
            mailDTO.setHasAttachment(MailAttachment.HAS_ATTACHMENT);
            mailDTO.setAttachmentUrl(attachmentUrl);
        } else {
            mailDTO.setHasAttachment(MailAttachment.NO_ATTACHMENT);
        }
        mailDTO.setMailNo(mailNo).setTo(to);
        return mailDTO;
    }
    /**
     * 接收人检查
     * @param: to
     * @return:
     * @throws: Exception
     */
    private static String[] receiverCheck(String to) throws Exception {
        String[] tempArray = to.split(";");
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
        return strArrLast;
    }

    /**
     * 附件检查
     * @param: mailNo
     * @param: fileArrayList
     * @return:
     * @throws: Exception
     */
    private static String[] attachmentCheck(String mailNo, MultipartFile[] fileArrayList) throws Exception {
        String[] attachmentUrlArrayList = new String[fileArrayList.length];
        for (int i = 0; i < fileArrayList.length; i++) {
            OutputStream out = null;
            try {
                byte[] bytes = fileArrayList[i].getBytes();
                //文件校验

                //对名字的操作
                String attachmentUrl = String.format("%s/%s", FILE_PATH, mailNo+"-"+fileArrayList[i].getOriginalFilename());
                attachmentUrlArrayList[i] = attachmentUrl;
                //创建文件
                File desc = getAbsoluteFile(attachmentUrl);
                //输出流
                out = new FileOutputStream(desc);
                out.write(bytes);
            } catch (Exception e) {
                throw new Exception("邮件存储失败");
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
        return attachmentUrlArrayList;
    }
    /**
     * 创建文件
     * @param: absolutePath
     * @return:
     * @throws: IOException
     */
    private static File getAbsoluteFile(String absolutePath) throws IOException {
        File desc = new File(absolutePath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }




}
