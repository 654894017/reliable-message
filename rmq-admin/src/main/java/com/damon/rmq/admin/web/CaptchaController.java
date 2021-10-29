package com.damon.rmq.admin.web;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.damon.rmq.admin.utils.CaptchaValidateUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 验证码控制器
 * 
 * @author xianping_lu
 *
 */
@Controller
public class CaptchaController {

    @RequestMapping(value = "login/captcha", method = RequestMethod.GET)
    public void captcha(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        ICaptcha captcha = CaptchaUtil.createCircleCaptcha(110, 30, 4, 5);
        // 禁止图像缓存
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream outputStream = response.getOutputStream();
        captcha.write(outputStream);
        // 将四位数字的验证码保存到Session中。
        session.setAttribute(CaptchaValidateUtil.SESSION_KEY, captcha.getCode());
        session.setAttribute(CaptchaValidateUtil.SESSION_TIME_KEY, System.currentTimeMillis());
        outputStream.flush();
        outputStream.close();
    }
}
