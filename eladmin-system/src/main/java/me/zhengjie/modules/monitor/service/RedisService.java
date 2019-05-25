package me.zhengjie.modules.monitor.service;

import org.springframework.data.domain.Pageable;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.zhengjie.modules.monitor.domain.vo.RedisVo;

/**
 * 可自行扩展
 * 
 * @author
 * @date 2018-12-10
 */
public interface RedisService
{
    
    /**
     * findById
     * 
     * @param key
     * @return
     */
    public Page<RedisVo> findByKey(String key, Pageable pageable);
    
    /**
     * create
     * 
     * @param redisVo
     */
    public void save(RedisVo redisVo);
    
    /**
     * delete
     * 
     * @param key
     */
    public void delete(String key);
    
    /**
     * 清空所有缓存
     */
    public void flushdb();
}
