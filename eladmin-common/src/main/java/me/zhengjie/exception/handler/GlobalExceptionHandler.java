package me.zhengjie.exception.handler;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.http.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author jie
 * @date 2018-11-23
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e){
        // 打印堆栈信息
        log.error(ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.code(HttpStatus.INTERNAL_SERVER_ERROR)
                // .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .i18n("msg.operation.fail")
                .build());
    }

    /**
     * 处理 接口无权访问异常AccessDeniedException
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException e){
        // 打印堆栈信息
        log.error(ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.code(HttpStatus.FORBIDDEN)
                .i18n("msg.operation.fail")
                .build());
    }

    /**
     * 处理自定义异常
     * @param e
     * @return
     */
	@ExceptionHandler(value = BadRequestException.class)
	public ResponseEntity<ApiResponse<?>> badRequestException(BadRequestException e) {
        // 打印堆栈信息
        log.error(ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.code(HttpStatus.BAD_REQUEST).message(e.getMessage()).build());
	}

    /**
     * 处理 EntityExist
     * @param e
     * @return
     */
    @ExceptionHandler(value = EntityExistException.class)
    public ResponseEntity<ApiResponse<?>> entityExistException(EntityExistException e) {
        // 打印堆栈信息
        log.error(ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.code(HttpStatus.BAD_REQUEST).message(e.getMessage()).build());
    }

    /**
     * 处理 EntityNotFound
     * @param e
     * @return
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> entityNotFoundException(EntityNotFoundException e) {
        // 打印堆栈信息
        log.error(ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.code(HttpStatus.NOT_FOUND).message(e.getMessage()).build());
    }

    /**
     * 处理所有接口数据验证异常
     * @param e
     * @returns
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        // 打印堆栈信息
        log.error(ExceptionUtils.getStackTrace(e));
        String[] str = e.getBindingResult().getAllErrors().get(0).getCodes()[1].split("\\.");
        StringBuffer msg = new StringBuffer(str[1] + ":");
        msg.append(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.code(HttpStatus.BAD_REQUEST).message(msg.toString()).build());
    }
    
}
