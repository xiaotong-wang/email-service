package com.otd.emaildemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * @ClassName: MailDTO
 * @Description:
 * @Author: xiaotong.wang
 * @Date: 2021/8/13 13:48
 */
@Data
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MailDTO {
    /**
     * 邮件编号
     */
    private String mailNo;
    /**
     * 收件人
     */
    private String[] to;
    /**
     * 主题
     */
    private String subject;
    /**
     * 邮件类型 1文本邮件 2模板邮件
     */
    private Integer mailType;
    /**
     * 文本内容
     */
    private String content;
    /**
     * 模板名称
     */
    private String modelName;
    /**
     * 模板内容
     */
    private Map<String, String> modelContent;
    /**
     * 发送次数
     */
    private Integer sendNum;
    /**
     * 发送状态 0未推送 1推送成功 2推送失败 -1失败不再发送
     */
    private Integer sendStatus;
    /**
     * 发送状态 0未推送 1推送成功 2推送失败 -1失败不再发送
     */
    private String errorMsg;
    /**
     * 附件 0没有 1有
     */
    private Integer hasAttachment;
    /**
     * 附件地址
     */
    private String[] attachmentUrl;
}
