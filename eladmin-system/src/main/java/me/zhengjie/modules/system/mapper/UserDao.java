package me.zhengjie.modules.system.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import me.zhengjie.modules.system.domain.User;

public interface UserDao extends BaseMapper<User>
{
    
    /**
     * 修改密码
     * 
     * @param id
     * @param pass
     * @param lastPasswordResetTime
     */
    @Update(value = "update sys_user set password = #{pass} , last_password_reset_time = #{lastPasswordResetTime} where id = #{id}")
    void updatePass(Long id, String pass, Date lastPasswordResetTime);
    
    /**
     * 修改头像
     * 
     * @param id
     * @param url
     */
    @Update(value = "update sys_user set avatar = #{url} where id = #{id}")
    void updateAvatar(Long id, String url);
    
    /**
     * 修改邮箱
     * 
     * @param id
     * @param email
     */
    @Update(value = "update sys_user set email = #{email} where id = #{id}")
    void updateEmail(Long id, String email);
    
    /**
     * 用户角色中间表
     * 
     * @param userId
     * @param roleId
     */
    @Insert(value = "insert into sys_users_roles(user_id, role_id) values(#{userId}, #{roleId})")
    void saveRole(Long userId, Long roleId);
    
    /**
     * 刪除用户角色
     * @param id
     */
    @Delete(value = "delete from sys_users_roles where user_id = #{id}")
    void deleteAllRole(Long id);
}
