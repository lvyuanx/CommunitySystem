package com.lyx.mail.Utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lvyx.commons.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * <p>
 * 邮件发送工具类
 * </p>
 *
 * @author lvyx
 * @since 2022-04-15 09:03:53
 */
@Slf4j
public class MailSenderUtils {

    private static final JavaMailSender javaMailSender;

    static{
        javaMailSender = ApplicationContextUtils.getBean(JavaMailSender.class);
    }

    /**
     * 发送普通邮件
     * @param from      发件人
     * @param to        收件人
     * @param subject   邮件主题
     * @param content   邮件内容
     * @author lvyx
     * @since 2022/4/15 9:22
     **/
    public static void sendSimpleMail(String from, String[] to , String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        //发件人
        String realFrom = from;
        if(StringUtils.isBlank(from)){
            realFrom = from;
        }
        message.setFrom(realFrom);
        //收信人
        message.setTo(to);
        //主题
        message.setSubject(subject);
        //文本
        message.setText(content);
        javaMailSender.send(message);
        log.info("发送邮件成功：发件人：{},收件人：{},主题：{}",realFrom, to, subject);
    }

    /**
     * 发送html邮件
     * @param from      发件人
     * @param to        收件人
     * @param subject   邮件主题
     * @param content   邮件内容
     * @author lvyx
     * @since 2022/4/15 9:26
     **/
    public static void sendHtmlMail(String from, String[] to , String subject, String content){
        //使用MimeMessage，MIME协议
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        String realFrom = from;
        try {
            helper = new MimeMessageHelper(message, true);
            if(StringUtils.isBlank(from)){
                realFrom = from;
            }
            helper.setFrom(realFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(message);
            log.info("发送HTML邮件成功：发件人：{},收件人：{},主题：{}",realFrom, to, subject);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            log.error("发送HTML邮件失败：发件人：{},收件人：{},主题：{}",realFrom, to, subject);
        }
    }

    /**
     * 发送带附件的邮件
     * @param from       发件人
     * @param to         收件人
     * @param subject    邮件主题
     * @param content    邮件内容
     * @param filePath   附件路径
     * @author lvyx
     * @since 2022/4/15 9:30
     **/
    public static void sendAttachmentMail(String from,String[] to,String subject, String content, String filePath) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        String realFrom = from;
        try {
            //true代表支持多组件，如附件，图片等
            helper = new MimeMessageHelper(message, true);
            if(StringUtils.isBlank(from)){
                realFrom = from;
            }
            helper.setFrom(realFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = file.getFilename();
            //添加附件，可多次调用该方法添加多个附件
            helper.addAttachment(fileName, file);
            javaMailSender.send(message);
            log.info("发送带附件邮件成功：发件人：{},收件人：{},主题：{}",realFrom, to, subject);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("发送带附件邮件成功：发件人：{},收件人：{},主题：{}",realFrom, to, subject);
        }
    }


    /**
     * 发送带图片的邮件
     * @param from       发件人
     * @param to         收件人
     * @param subject    邮件主题
     * @param content    内容
     * @param rscPath    图片路径
     * @param rscId      图片id
     * @author lvyx
     * @since 2022/4/15 9:35
     **/
    public static void sendInlineResourceMail(String from,String[] to, String subject, String content, String rscPath, String rscId) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String realFrom = from;
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            if(StringUtils.isBlank(from)){
                realFrom = from;
            }
            helper.setFrom(realFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            //重复使用添加多个图片
            helper.addInline(rscId, res);
            javaMailSender.send(message);
            log.info("发送带图片邮件成功：发件人：{},收件人：{},主题：{}",realFrom, to, subject);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("发送带图片邮件成功：发件人：{},收件人：{},主题：{}",realFrom, to, subject);
        }
    }



}