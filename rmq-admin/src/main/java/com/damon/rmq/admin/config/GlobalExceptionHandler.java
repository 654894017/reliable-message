package com.damon.rmq.admin.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.damon.rmq.api.exceptions.CheckException;
import com.damon.rmq.api.exceptions.RmqException;
import com.damon.rmq.api.model.dto.RspBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 控制器异常处理
 *
 * @author xianpinglu
 */
@SuppressWarnings("unchecked")
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 控制器异常处理入口
     *
     * @param e 异常信息
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RspBase<Void> resolveException(Exception e, HttpServletRequest request) {
        log.error(e.getClass().getName());
        RspBase<Void> retBean = new RspBase<Void>();

        if (e instanceof BindException) {
            BindException exs = (BindException) e;
            log.error("【参数校验异常】:" + e);

            List<FieldError> errors = exs.getFieldErrors();

            if (errors.size() > 0) {
                FieldError error = errors.get(0);
                retBean.code(1).msg(error.getField() + error.getDefaultMessage());
            }
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exs = (MethodArgumentNotValidException) e;
            log.error("【参数校验异常】:" + e);

            List<FieldError> errors = exs.getBindingResult().getFieldErrors();

            if (errors.size() > 0) {
                FieldError error = errors.get(0);
                retBean.code(1).msg(error.getField() + error.getDefaultMessage());
            }
        } else if (ExceptionUtil.isCausedBy(e, CheckException.class)) {
            CheckException exception = (CheckException) ExceptionUtil.getCausedBy(e, CheckException.class);
            log.error("【校验异常】:" + exception.getMsg());
            retBean.code(exception.getCode()).msg(exception.getMsg());
        } else if (ExceptionUtil.isCausedBy(e, RmqException.class)) {
            RmqException exception = (RmqException) ExceptionUtil.getCausedBy(e, RmqException.class);
            log.error("【RMQ异常】:" + exception.getMsg(), e);
            retBean.code(exception.getCode()).msg(exception.getMsg());
        } else {
            log.error(e.getMessage(), e);
            retBean.code(1).msg("系统异常");
        }

        return retBean;
    }
}
