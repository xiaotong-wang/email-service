package com.otd.emaildemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @ClassName: MailEntity
 * @Description:
 * @Author: xiaotong.wang
 * @Date: 2021/8/13 11:24
 */
@Data
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    /**
     * 收件人
     */
    private String to;
    /**
     * 主题
     */
    private String subject;
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
}
