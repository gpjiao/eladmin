package me.zhengjie.aspect;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import me.zhengjie.http.ApiResponse;
import me.zhengjie.utils.HttpLocaleUtil;

@Aspect
@Component
@Order(1)
public class I18nAspect
{
    private Logger logger = LoggerFactory.getLogger(I18nAspect.class);
    
    @Autowired
    protected MessageSource messageSource;
    
    /**
     * 
     * @title 定义切入点
     * @description <功能详细描述>
     * @date 2019年3月12日 上午10:36:03
     */
    @Pointcut("execution(public me.zhengjie.http.ApiResponse me.zhengjie..*.*(..)) || execution(public org.springframework.http.ResponseEntity me.zhengjie.exception.handler..*.*(..))")
    public void i18nPointCut()
    {
    }
    
    /**
     * 
     * @title 后置返回通知
     * @description 返回message i18n
     * @date 2019年3月12日 上午10:34:54
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "i18nPointCut()")
    public void afterReturning(Object ret)
        throws Throwable
    {
        // 处理完请求，返回内容
        try
        {
            ApiResponse<?> apiResponse = null;
            if (ret instanceof ResponseEntity)
            {
                Object obj = ((ResponseEntity<?>)ret).getBody();
                if (obj instanceof ApiResponse)
                {
                    apiResponse = (ApiResponse<?>)obj;
                }
            }
            else if (ret instanceof ApiResponse)
            {
                apiResponse = (ApiResponse<?>)ret;
            }
            
            if (null != apiResponse && StringUtils.isBlank(apiResponse.getMessage()))
            {
                if (StringUtils.isNotBlank(apiResponse.getI18n()))
                {
                    ServletRequestAttributes attributes =
                        (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
                    HttpServletRequest request = attributes.getRequest();
                    apiResponse.setLocale(HttpLocaleUtil.getLanguage(request));
                    apiResponse
                        .setMessage(messageSource.getMessage(apiResponse.getI18n(), null, apiResponse.getLocale()));
                }
                else
                {
                    apiResponse.setMessage(HttpStatus.valueOf(apiResponse.getCode()).getReasonPhrase());
                }
            }
        }
        catch (Exception e)
        {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
    
}
