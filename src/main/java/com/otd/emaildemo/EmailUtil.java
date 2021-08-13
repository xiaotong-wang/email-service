//package org.autosys.common.util;
//
//import com.alibaba.fastjson.JSON;
//import freemarker.core.ParseException;
//import freemarker.template.MalformedTemplateNameException;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import freemarker.template.TemplateNotFoundException;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang3.time.DateFormatUtils;
//import org.apache.commons.lang3.time.DateUtils;
//import org.autosys.common.api.vo.Result;
//import org.autosys.common.model.SysMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.mail.MailException;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//public class EmailUtil {
//    //系统管理服务
//    @Value("${service.jop-system}")
//    private String serverJopSystem;
//
//    @Value("${email.InternetAddress}")
//    private String InternetAddress;
//
//    @Autowired
//    private FreeMarkerConfigurer freeMarkerConfigurer;
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//
//    /**
//     * @Title: sendEmailWithTemplate
//     * @Description: 发送邮件提醒
//     * @param esTitle 消息标题
//     * @param esparamMap  推送所需参数Json格式
//     * @param esReceiver 接收人
//     * @param remark  备注
//     * @param realTimePush  是否实时推送邮件：boolean:true/false
//     * @param templateFileName 模板文件信息
//     * @return: Result
//     * @date: 2020年6月3日 19点32分
//     * @throws
//     * @author: jingqiu.wang
//     * @throws Exception
//     */
//    public Result sendEmailWithTemplate(String esTitle, Map<String,Object> esparamMap, String esReceiver, String remark, boolean realTimePush, String templateFileName){
//        Result result = new Result();
//        //邮件信息组装
//        SysMessage sysMessage = new SysMessage();
//        sysMessage.setEsTitle(esTitle);
//        sysMessage.setEsParam(JSON.toJSONString(esparamMap));
//        sysMessage.setEsReceiver(esReceiver);
//        sysMessage.setRemark(remark);
//        sysMessage.setRealTimePush(realTimePush);
//        sysMessage.setTemplateFileName(templateFileName);
//        try {
//        //获取模板信息
//        if(StringUtils.isEmpty(templateFileName)){
//            log.error("模板文件名称为空异常");
//        }
//        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateFileName);
//        if(null == template){
//            log.error("模板文件获取异常");
//        }
//        //邮件内容处理
//        String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, esparamMap);
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//        mimeMessageHelper.setSubject(esTitle);
//        // 发件人
//        InternetAddress from = new InternetAddress(InternetAddress);
//        mimeMessageHelper.setFrom(from);
//        // 添加默认收件人
//        String receiverLists = sysMessage.getEsReceiver();
//        if(org.apache.commons.lang3.StringUtils.isBlank(receiverLists)){
//            log.error("收件人为空异常");
//        }
//        if(receiverLists.indexOf(";") > 0){
//            String [] tempArray = receiverLists.split(";");
//            // step1: 定义一个list列表，并循环赋值
//            ArrayList<String> strList = new ArrayList<String>();
//            for(int i = 0; i < tempArray.length; i++){
//                //去除空的
//                if(tempArray[i] == null || tempArray[i].length() < 1){
//                    continue;
//                }else{
//                    strList.add(tempArray[i]);
//                }
//            }
//            if(strList.size() < 1){
//                //收件人为空，则返回
//                log.error("收件人为空异常");
//            }
//            // step2: 把list列表转换给一个新定义的中间数组，并赋值给它
//            String strArrLast[] = strList.toArray(new String [strList.size()]);
//            // 添加收件人
//            mimeMessageHelper.setTo(strArrLast);
//        }else{
//            mimeMessageHelper.setTo(sysMessage.getEsReceiver());
//        }
//        // 添加内容
//        mimeMessageHelper.setText(emailContent, true);
//        // 发送邮件
//
//            javaMailSender.send(mimeMessage);
//            sysMessage.setEsSendStatus("1");
//            sysMessage.setEsSendTime(DateUtils.getDate());
//            sysMessage.setEsType("10020002");
//            result.setSuccess(true);
//        } catch (MailException e) {
//            result.setSuccess(false);
//            sysMessage.setEsSendStatus("2");
//            sysMessage.setEsSendTime(DateUtils.getDate());
//            sysMessage.setEsType("10020002");
//            e.printStackTrace();
//            log.error("发送邮件提醒异常");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (MalformedTemplateNameException e) {
//            e.printStackTrace();
//        } catch (TemplateNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//            HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(sysMessage),headers);
//            result = restTemplate.postForObject(serverJopSystem + "/message/sysMessageRestful/add",requestEntity,Result.class);
//        }
//        return result;
//    }
//
//    /**
//     * @Title: sendEmailWithContent
//     * @Description: 发送邮件提醒
//     * @param esTitle 消息标题
//     * @param esReceiver 接收人
//     * @param remark  备注
//     * @param realTimePush  是否实时推送邮件：boolean:true/false
//     * @param esContent 邮件内容
//     * @return: Result
//     * @date: 2020年6月3日 19点32分
//     * @throws
//     * @author: jingqiu.wang
//     * @throws Exception
//     */
//    public Result sendEmailWithContent(String esTitle, String esReceiver, String remark, boolean realTimePush, String esContent){
//        Result result = new Result();
//        //邮件信息组装
//        SysMessage sysMessage = new SysMessage();
//        sysMessage.setEsTitle(esTitle);
//        sysMessage.setEsContent(esContent);
//        sysMessage.setEsReceiver(esReceiver);
//        sysMessage.setRemark(remark);
//        sysMessage.setRealTimePush(realTimePush);
//        try {
//
//            //邮件内容处理
//            String emailContent = esContent;
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//            mimeMessageHelper.setSubject(esTitle);
//            // 发件人
//            InternetAddress from = new InternetAddress(InternetAddress);
//            mimeMessageHelper.setFrom(from);
//            // 添加默认收件人
//            String receiverLists = sysMessage.getEsReceiver();
//            if(org.apache.commons.lang3.StringUtils.isBlank(receiverLists)){
//                log.error("收件人为空异常");
//            }
//            if(receiverLists.indexOf(";") > 0){
//                String [] tempArray = receiverLists.split(";");
//                // step1: 定义一个list列表，并循环赋值
//                ArrayList<String> strList = new ArrayList<String>();
//                for(int i = 0; i < tempArray.length; i++){
//                    //去除空的
//                    if(tempArray[i] == null || tempArray[i].length() < 1){
//                        continue;
//                    }else{
//                        strList.add(tempArray[i]);
//                    }
//                }
//                if(strList.size() < 1){
//                    //收件人为空，则返回
//                    log.error("收件人为空异常");
//                }
//                // step2: 把list列表转换给一个新定义的中间数组，并赋值给它
//                String strArrLast[] = strList.toArray(new String [strList.size()]);
//                // 添加收件人
//                mimeMessageHelper.setTo(strArrLast);
//            }else{
//                mimeMessageHelper.setTo(sysMessage.getEsReceiver());
//            }
//            // 添加内容
//            mimeMessageHelper.setText(emailContent, true);
//            // 发送邮件
//
//            javaMailSender.send(mimeMessage);
//            sysMessage.setEsSendStatus("1");
//            sysMessage.setEsSendTime(DateUtils.getDate());
//            sysMessage.setEsType("10020002");
//            result.setSuccess(true);
//        } catch (MailException e) {
//            result.setSuccess(false);
//            sysMessage.setEsSendStatus("2");
//            sysMessage.setEsSendTime(DateUtils.getDate());
//            sysMessage.setEsType("10020002");
//            e.printStackTrace();
//            log.error("发送邮件提醒异常");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }  catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//            HttpEntity<String> requestEntity = new HttpEntity<String>(JSON.toJSONString(sysMessage),headers);
//            result = restTemplate.postForObject(serverJopSystem + "/message/sysMessageRestful/add",requestEntity,Result.class);
//        }
//        return result;
//    }
//}
//
//    private Result sendNoticeEmailMethod(CarCheck carCheck, CarFeedback carFeedback) throws Exception {
//        //获取供应商ID
//        //获取供应商岗位用户邮箱信息
//        QueryWrapper<SupplierPosition> supplierPositionQueryWrapper = new QueryWrapper<>();
//        supplierPositionQueryWrapper.eq("SUPPLIER_CODE",carFeedback.getSupplierCode());
//        supplierPositionQueryWrapper.eq("DEL_FLAG",0);
//        supplierPositionQueryWrapper.eq("ISEMAIL",1);
//        List<SupplierPosition> supplierPositionList = supplierPositionService.list(supplierPositionQueryWrapper);
//        List<String> emailList = supplierPositionList.stream().map(SupplierPosition::getEmail).collect(Collectors.toList());
//        String emailListStr = StringUtils.join(emailList,";");
//        if(null == emailListStr){
//            log.error("未找到供应商用户信息异常");
//        }
//
//        String title = "特殊车供货核查反馈通知";
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("supplierName", carFeedback.getSupplierName());
//        paramMap.put("knrNumber", carFeedback.getKnrNumber());
//        paramMap.put("phasesName", carFeedback.getCarType());
//        paramMap.put("feedbackDate", DateFormatUtils.format(org.apache.commons.lang3.time.DateUtils.addWeeks(new Date(), 1), "yyyy年MM月dd日"));
//        String receiver = emailListStr;
//        String remark = "";
//        boolean realTimePush = true;
//        String templateFileName = "supplyCheckFeedBackTemplate.ftl";
//        return emailUtil.sendEmailWithTemplate(title, paramMap, receiver, remark, realTimePush, templateFileName);
//    }
