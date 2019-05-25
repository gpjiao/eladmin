package me.zhengjie.http;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ApiResponse<T>
{
    protected Object code;
    
    @Setter
    protected String message;
    
    @Nullable
    protected T content;
    
    private Integer current;
    
    private Integer total;
    
    private Integer size;
    
    @Setter
    protected Locale locale = Locale.getDefault();
    
    @JSONField(serialize = false)
    protected String i18n;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private ApiResponse(Object code, String i18n, String message, @Nullable T content)
    {
        this.code = code;
        this.i18n = i18n;
        this.message = message;
        this.content = content;
    }
    
    private ApiResponse(Object code, String i18n, String message, @Nullable IPage<T> page)
    {
        this.code = code;
        this.i18n = i18n;
        this.message = message;
        if (null == page)
        {
            return;
        }
        this.current = (int)page.getCurrent();
        this.size = (int)page.getSize();
        this.total = (int)page.getTotal();
        if (CollectionUtils.isNotEmpty(page.getRecords()))
        {
            this.content = (T)page.getRecords();
        }
    }
    
    public static BodyBuilder code(HttpStatus code)
    {
        return new DefaultBuilder(code);
    }
    
    public static BodyBuilder code(int code)
    {
        return new DefaultBuilder(code);
    }
    
    public interface BodyBuilder
    {
        BodyBuilder i18n(String i18n);
        
        BodyBuilder message(String message);
        
        <T> ApiResponse<T> build();
        
        <T> ApiResponse<T> body(@Nullable T body);
        
        <T> ApiResponse<List<T>> page(@Nullable IPage<T> page);
    }
    
    private static class DefaultBuilder implements BodyBuilder
    {
        private final Object statusCode;
        
        private String i18n;
        
        private String message;
        
        public DefaultBuilder(Object statusCode)
        {
            this.statusCode = statusCode;
        }
        
        @Override
        public BodyBuilder i18n(String i18n)
        {
            this.i18n = i18n;
            return this;
        }
        
        @Override
        public BodyBuilder message(String message)
        {
            this.message = message;
            return this;
        }
        
        @Override
        public <T> ApiResponse<T> build()
        {
            return body(null);
        }
        
        @Override
        public <T> ApiResponse<T> body(@Nullable T content)
        {
            return new ApiResponse<>(this.statusCode, this.i18n, this.message, content);
        }
        
        @Override
        public <T> ApiResponse<List<T>> page(@Nullable IPage<T> page)
        {
            return new ApiResponse(this.statusCode, i18n, this.message, page);
        }
    }
    
    public int getCode()
    {
        if (this.code instanceof HttpStatus)
        {
            return ((HttpStatus)this.code).value();
        }
        else
        {
            return (Integer)this.code;
        }
    }
    
    @Data
    private static class TestEntity
    {
        private String name;
        
        private String value;
    }
    
    public static void main(String[] args)
    {
        ApiResponse<String> entity1 = ApiResponse.code(HttpStatus.OK).body("String");
        System.out.println(JSON.toJSONString(entity1));
        
        IPage<TestEntity> page = new Page<TestEntity>(1, 10);
        String json = "[{\"name\":\"echo\",\"value\":\"15\"},{\"name\":\"test\",\"value\":\"21\"}]";
        page.setRecords(JSON.parseArray(json, TestEntity.class));
        
        ApiResponse<List<TestEntity>> entity2 = ApiResponse.code(HttpStatus.OK).page(page);
        System.out.println(JSON.toJSONString(entity2));
    }
    
}
