package me.zhengjie.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import me.zhengjie.domain.VerificationCode;
import me.zhengjie.domain.vo.EmailVo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.mapper.VerificationCodeDao;
import me.zhengjie.service.VerificationCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * @author
 * @date 2018-12-26
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VerificationCodeServiceImpl implements VerificationCodeService
{
    
    @Autowired
    private VerificationCodeDao verificationCodeDao;
    
    @Value("${code.expiration}")
    private Integer expiration;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmailVo sendEmail(VerificationCode code)
    {
        EmailVo emailVo = null;
        String content = "";
        VerificationCode verificationCode =
            verificationCodeDao.selectOne(new QueryWrapper<VerificationCode>().eq("scenes", code.getScenes())
                .eq("type", code.getType())
                .eq("value", code.getValue())
                .eq("status", true));
        // 如果不存在有效的验证码，就创建一个新的
        TemplateEngine engine =
            TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/email.ftl");
        if (verificationCode == null)
        {
            code.setCode(RandomUtil.randomNumbers(6));
            content = template.render(Dict.create().set("code", code.getCode()));
            emailVo = new EmailVo(Arrays.asList(code.getValue()), "eladmin后台管理系统", content);
            verificationCodeDao.insert(code);
            timedDestruction(code);
            // 存在就再次发送原来的验证码
        }
        else
        {
            content = template.render(Dict.create().set("code", verificationCode.getCode()));
            emailVo = new EmailVo(Arrays.asList(verificationCode.getValue()), "eladmin后台管理系统", content);
        }
        return emailVo;
    }
    
    @Override
    public void validated(VerificationCode code)
    {
        VerificationCode verificationCode =
            verificationCodeDao.selectOne(new QueryWrapper<VerificationCode>().eq("scenes", code.getScenes())
                .eq("type", code.getType())
                .eq("value", code.getValue())
                .eq("status", true));
        if (verificationCode == null || !verificationCode.getCode().equals(code.getCode()))
        {
            throw new BadRequestException("无效验证码");
        }
        else
        {
            verificationCode.setStatus(false);
            verificationCodeDao.updateById(verificationCode);
        }
    }
    
    /**
     * 定时任务，指定分钟后改变验证码状态
     * 
     * @param verifyCode
     */
    private void timedDestruction(VerificationCode verifyCode)
    {
        // 以下示例为程序调用结束继续运行
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        try
        {
            executorService.schedule(() -> {
                verifyCode.setStatus(false);
                verificationCodeDao.updateById(verifyCode);
            }, expiration * 60 * 1000L, TimeUnit.MILLISECONDS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
