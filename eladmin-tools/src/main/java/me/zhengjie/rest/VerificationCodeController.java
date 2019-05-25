package me.zhengjie.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import me.zhengjie.domain.VerificationCode;
import me.zhengjie.domain.vo.EmailVo;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.service.EmailService;
import me.zhengjie.service.VerificationCodeService;
import me.zhengjie.utils.ElAdminConstant;

/**
 * @author
 * @date 2018-12-26
 */
@RestController
@RequestMapping("admin")
public class VerificationCodeController
{
    
    @Autowired
    private VerificationCodeService verificationCodeService;
    
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
    
    @Autowired
    private EmailService emailService;
    
    @PostMapping(value = "/code/resetEmail")
    public ApiResponse<?> resetEmail(@RequestBody VerificationCode code)
        throws Exception
    {
        code.setScenes(ElAdminConstant.RESET_MAIL);
        EmailVo emailVo = verificationCodeService.sendEmail(code);
        emailService.send(emailVo, emailService.find());
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").build();
    }
    
    @PostMapping(value = "/code/email/resetPass")
    public ApiResponse<?> resetPass(@RequestParam String email)
        throws Exception
    {
        VerificationCode code = new VerificationCode();
        code.setType("email");
        code.setValue(email);
        code.setScenes(ElAdminConstant.RESET_MAIL);
        EmailVo emailVo = verificationCodeService.sendEmail(code);
        emailService.send(emailVo, emailService.find());
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").build();
    }
    
    @GetMapping(value = "/code/validated")
    public ApiResponse<?> validated(VerificationCode code)
    {
        verificationCodeService.validated(code);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").build();
    }
}
