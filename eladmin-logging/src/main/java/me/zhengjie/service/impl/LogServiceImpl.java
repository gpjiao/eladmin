package me.zhengjie.service.impl;

import cn.hutool.json.JSONObject;
import me.zhengjie.domain.Log;
import me.zhengjie.mapper.LogDao;
import me.zhengjie.service.LogService;
import me.zhengjie.utils.RequestHolder;
import me.zhengjie.utils.SecurityContextHolder;
import me.zhengjie.utils.StringUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author jie
 * @date 2018-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logDao;
    
    @Value("${jwt.header}")
    private String tokenHeader;

    private final String LOGINPATH = "login";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ProceedingJoinPoint joinPoint, Log log){

        // 获取request
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        me.zhengjie.aop.log.Log aopLog = method.getAnnotation(me.zhengjie.aop.log.Log.class);

        // 描述
        if (log != null) {
            log.setDescription(aopLog.value());
        }

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";

        String params = "{";
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        // 用户名
        String username = "";

        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                params += " " + argNames[i] + ": " + argValues[i];
            }
        }

        // 获取IP地址
        log.setRequestIp(StringUtils.getIP(request));

        if(!LOGINPATH.equals(signature.getName())){
            UserDetails userDetails = SecurityContextHolder.getUserDetails();
            username = userDetails.getUsername();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(argValues[0]);
                username = jsonObject.get("username").toString();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.setMethod(methodName);
        log.setUsername(username);
        log.setParams(params + " }");
        logDao.insert(log);
    }
    
    @Override
    public IPage<Log> findAll(int pageNum, int pageSize, Log log)
    {
        QueryWrapper<Log> wrapper = new QueryWrapper<Log>();
        if (StringUtils.isNotBlank(log.getUsername()))
        {
            wrapper.like("username", log.getUsername());
        }
        
        if (StringUtils.isNotBlank(log.getLogType()))
        {
            wrapper.eq("log_type", log.getLogType());
        }
        return logDao.selectPage(new Page<Log>(pageNum, pageSize), wrapper.orderByDesc("id"));
    }
}
