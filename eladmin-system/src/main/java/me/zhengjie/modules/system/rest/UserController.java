package me.zhengjie.modules.system.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;

import me.zhengjie.aop.log.Log;
import me.zhengjie.domain.Picture;
import me.zhengjie.domain.VerificationCode;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.http.ApiResponse;
import me.zhengjie.modules.security.security.JwtUser;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.service.PictureService;
import me.zhengjie.service.VerificationCodeService;
import me.zhengjie.utils.ElAdminConstant;
import me.zhengjie.utils.EncryptUtils;
import me.zhengjie.utils.SecurityContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * @author
 * @date 2018-11-23
 */
@RestController
@RequestMapping("admin")
public class UserController
{
    @Autowired
    DataSource dataSource;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PictureService pictureService;
    
    @Autowired
    private VerificationCodeService verificationCodeService;
    
    private static final String ENTITY_NAME = "user";
    
    @Log("查询用户")
    @GetMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    public ApiResponse<List<User>> getUsers(UserDTO userDTO, Pageable pageable)
    {
        IPage<User> page = userService.queryAll(pageable.getPageNumber(), pageable.getPageSize(), userDTO);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").page(page);
    }
    
    @Log("新增用户")
    @PostMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_CREATE')")
    public ApiResponse<UserDTO> create(@Validated @RequestBody User resources)
    {
        if (resources.getId() != null)
        {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        UserDTO userDTO = userService.create(resources);
        return ApiResponse.code(HttpStatus.CREATED).i18n("msg.operation.success").body(userDTO);
    }
    
    @Log("修改用户")
    @PutMapping(value = "/users")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT')")
    public ApiResponse<?> update(@Validated(User.Update.class) @RequestBody User resources)
    {
        userService.update(resources);
        return ApiResponse.code(HttpStatus.NO_CONTENT).i18n("msg.operation.update.success").build();
    }
    
    @Log("删除用户")
    @DeleteMapping(value = "/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_DELETE')")
    public ApiResponse<?> delete(@PathVariable Long id)
    {
        userService.delete(id);
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.delete.success").build();
    }
    
    /**
     * 验证密码
     * 
     * @param pass
     * @return
     */
    @GetMapping(value = "/users/validPass/{pass}")
    public ApiResponse<Map<String, Integer>> validPass(@PathVariable String pass)
    {
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        JwtUser jwtUser = (JwtUser)userDetailsService.loadUserByUsername(userDetails.getUsername());
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("status", 200);
        if (!jwtUser.getPassword().equals(EncryptUtils.encryptPassword(pass)))
        {
            map.put("status", 400);
        }
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.success").body(map);
    }
    
    /**
     * 修改密码
     * 
     * @param pass
     * @return
     */
    @GetMapping(value = "/users/updatePass/{pass}")
    public ApiResponse<?> updatePass(@PathVariable String pass)
    {
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        JwtUser jwtUser = (JwtUser)userDetailsService.loadUserByUsername(userDetails.getUsername());
        if (jwtUser.getPassword().equals(EncryptUtils.encryptPassword(pass)))
        {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(jwtUser, EncryptUtils.encryptPassword(pass));
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.update.success").build();
    }
    
    /**
     * 修改头像
     * 
     * @param file
     * @return
     */
    @PostMapping(value = "/users/updateAvatar")
    public ApiResponse<?> updateAvatar(@RequestParam MultipartFile file)
    {
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        JwtUser jwtUser = (JwtUser)userDetailsService.loadUserByUsername(userDetails.getUsername());
        Picture picture = pictureService.upload(file, jwtUser.getUsername());
        userService.updateAvatar(jwtUser, picture.getUrl());
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.update.success").build();
    }
    
    /**
     * 修改邮箱
     * 
     * @param user
     * @param user
     * @return
     */
    @PostMapping(value = "/users/updateEmail/{code}")
    public ApiResponse<?> updateEmail(@PathVariable String code, @RequestBody User user)
    {
        UserDetails userDetails = SecurityContextHolder.getUserDetails();
        JwtUser jwtUser = (JwtUser)userDetailsService.loadUserByUsername(userDetails.getUsername());
        if (!jwtUser.getPassword().equals(EncryptUtils.encryptPassword(user.getPassword())))
        {
            throw new BadRequestException("密码错误");
        }
        VerificationCode verificationCode =
            new VerificationCode(code, ElAdminConstant.RESET_MAIL, "email", user.getEmail());
        verificationCodeService.validated(verificationCode);
        userService.updateEmail(jwtUser, user.getEmail());
        return ApiResponse.code(HttpStatus.OK).i18n("msg.operation.update.success").build();
    }
    
}
