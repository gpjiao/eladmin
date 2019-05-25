package me.zhengjie.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

public class HttpLocaleUtil
{
    public static Locale getLanguage(HttpServletRequest request)
    {
        Locale locale = Locale.getDefault();
        if (null != request)
        {
            // 示例：zh-CN,zh-TW;q=0.9,zh;q=0.8,en-US;q=0.7,en;q=0.6
            String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
            if (StringUtils.isNotBlank(acceptLanguage))
            {
                String[] localeArray = acceptLanguage.split(",");
                
                if (null != localeArray && localeArray.length > 0)
                {
                    String localeStr = localeArray[0];
                    String[] localeStrArr = localeStr.split("-");
                    if (null != localeStr)
                    {
                        if (localeStrArr.length == 1)
                        {
                            locale = new Locale(localeStrArr[0]);
                        }
                        else if (localeStrArr.length == 2)
                        {
                            locale = new Locale(localeStrArr[0], localeStrArr[1]);
                        }
                    }
                }
            }
        }
        return locale;
    }
    
    /**
     * 方言字符串（格式：语言-国家，国家可选；示例：zh-CN）转方言
     * 
     * @param _locale
     * @return
     */
    public static Locale getLanguage(String _locale)
    {
        Locale locale = null;
        if (StringUtils.isNotBlank(_locale))
        {
            // 示例：zh-CN
            String[] localeArray = _locale.split(",");
            
            if (null != localeArray && localeArray.length > 0)
            {
                String localeStr = localeArray[0];
                String[] localeStrArr = localeStr.split("-");
                if (null != localeStr)
                {
                    if (localeStrArr.length == 1)
                    {
                        locale = new Locale(localeStrArr[0]);
                    }
                    else if (localeStrArr.length == 2)
                    {
                        locale = new Locale(localeStrArr[0], localeStrArr[1]);
                    }
                }
            }
        }
        return locale;
    }
}
