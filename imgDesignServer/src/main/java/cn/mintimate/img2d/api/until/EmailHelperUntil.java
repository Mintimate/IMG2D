package cn.mintimate.img2d.api.until;

import cn.mintimate.img2d.api.vo.RegisterUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * 发送邮件
 *
 * @author Mintimate
 */
@Component
@Slf4j
public class EmailHelperUntil {
    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderMailAddress;

    /**
     * 发送注册验证邮件
     * @param registerUserVO
     * @param UrlParameter
     */
    public void sendSimpleMail(RegisterUserVO registerUserVO,String UrlParameter){
        MimeMessage mimeMessage = null;
        try{
            mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);  // true 表示多部分，可添加内联资源
            // 设置邮件信息
            mimeMessageHelper.setFrom(senderMailAddress);
            mimeMessageHelper.setTo(registerUserVO.getUserEmail());
            mimeMessageHelper.setSubject("IMG2D注册验证");
            // 利用 Thymeleaf 引擎渲染 HTML
            Context context = new Context();
            context.setVariable("userName",registerUserVO.getUsername());
            context.setVariable("url","https://img2d.flyinbug.cn/checkData/register/"+UrlParameter);
            String content = this.templateEngine.process("register",context);  // 模板设置为 "mail"
            // 设置邮件内容
            mimeMessageHelper.setText(content,true); // true 表示开启 html
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            log.error("发送邮件出错：" + e.getMessage() + e.getCause());
        }
    }
}
