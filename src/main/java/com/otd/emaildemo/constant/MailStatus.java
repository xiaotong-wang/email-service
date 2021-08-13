package com.otd.emaildemo.constant;

/**
 * @ClassName: MailCode
 * @Description:
 * @Author: xiaotong.wang
 * @Date: 2021/8/13 15:16
 */
public abstract class MailStatus {
    /**
     * 待发送
     */
    public static final Integer READY_TO_SEND = 0;
    /**
     * 发送成功
     */
    public static final Integer SUCCESS = 1;
    /**
     * 发送失败，待重试
     */
    public static final Integer FAIL = 2;
    /**
     * 发送失败，不再重试
     */
    public static final Integer FAIL_FINAL = -1;
}
