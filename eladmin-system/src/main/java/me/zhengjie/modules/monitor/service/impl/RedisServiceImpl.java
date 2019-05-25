package me.zhengjie.modules.monitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.modules.monitor.domain.vo.RedisVo;
import me.zhengjie.modules.monitor.service.RedisService;
import me.zhengjie.utils.PageUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date 2018-12-10
 */
@Service
public class RedisServiceImpl implements RedisService
{
    
    @Autowired
    JedisPool pool;
    
    @SuppressWarnings("unchecked")
    @Override
    public Page<RedisVo> findByKey(String key, Pageable pageable)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            List<RedisVo> redisVos = new ArrayList<>();
            
            if (!key.equals("*"))
            {
                key = "*" + key + "*";
            }
            for (String s : jedis.keys(key))
            {
                RedisVo redisVo = new RedisVo(s, jedis.get(s));
                redisVos.add(redisVo);
            }
            Page<RedisVo> page = new Page<RedisVo>(pageable.getPageNumber(), pageable.getPageSize(), redisVos.size());
            page.setRecords(PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), redisVos));
            return page;
        }
        finally
        {
            if (null != jedis)
            {
                jedis.close(); // 释放资源还给连接池
            }
        }
        
    }
    
    @Override
    public void save(RedisVo redisVo)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.set(redisVo.getKey(), redisVo.getValue());
        }
        finally
        {
            if (null != jedis)
            {
                jedis.close(); // 释放资源还给连接池
            }
        }
    }
    
    @Override
    public void delete(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.del(key);
        }
        finally
        {
            if (null != jedis)
            {
                jedis.close(); // 释放资源还给连接池
            }
        }
        
    }
    
    @Override
    public void flushdb()
    {
        Jedis jedis = null;
        try
        {
            jedis = pool.getResource();
            jedis.flushDB();
        }
        finally
        {
            if (null != jedis)
            {
                jedis.close(); // 释放资源还给连接池
            }
        }
        
    }
}
