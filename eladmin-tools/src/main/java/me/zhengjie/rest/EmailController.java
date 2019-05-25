package me.zhengjie.rest;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.aop.log.Log;
import me.zhengjie.domain.EmailConfig;
import me.zhengjie.domain.vo.EmailVo;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 发送邮件
 * 
 * @author 郑杰
 * @date 2018/09/28 6:55:53
 */
@Slf4j
@RestController
@RequestMapping("admin")
public class EmailController
{
    
    @Autowired
    private EmailService emailService;
    
    @GetMapping(value = "/email")
    public ApiResponse<EmailConfig> get()
    {
        EmailConfig emailConfig = emailService.find();
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(emailConfig);
    }
    
    @Log("配置邮件")
    @PutMapping(value = "/email")
    public ApiResponse<?> emailConfig(@Validated @RequestBody EmailConfig emailConfig)
    {
        emailService.update(emailConfig, emailService.find());
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").build();
    }
    
    @Log("发送邮件")
    @PostMapping(value = "/email")
    public ApiResponse<?> send(@Validated @RequestBody EmailVo emailVo)
        throws Exception
    {
        log.warn("REST request to send Email : {}" + emailVo);
        emailService.send(emailVo, emailService.find());
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").build();
    }
}
