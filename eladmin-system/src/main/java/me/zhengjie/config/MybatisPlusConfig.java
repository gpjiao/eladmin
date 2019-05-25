package me.zhengjie.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;

/**
 * MybatisPlus配置
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年9月3日]
 */
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = {"me.zhengjie.**.mapper"})
public class MybatisPlusConfig
{
    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor()
    {
        return new PaginationInterceptor();
    }
    
    /**
     * 逻辑删除
     * 
     * @return
     */
    @Bean
    public ISqlInjector sqlInjector()
    {
        return new LogicSqlInjector();
    }
    
    /**
     * SQL执行效率插件
     */
    @Bean
    @Profile({"dev", "test"}) // 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor()
    {
        return new PerformanceInterceptor().setMaxTime(5000).setFormat(true);
    }
}
