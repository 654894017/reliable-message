package com.damon.rmq.admin.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 图形验证码工具类
 *
 * @author xianpinglu
 */
public final class CaptchaValidateUtil {

    public static final String SESSION_KEY = "validate_code";
    public static final String SESSION_TIME_KEY = "validate_code_time";
    /**
     * 设置验证码的有效时间 3分钟
     */
    public static final int TIMEOUT = 1000 * 60 * 3;
    private CaptchaValidateUtil() {
        throw new RuntimeException("CaptchaUtil.class can't be instantiated");
    }

    /**
     * <p>验证码合法性校验</p>
     *
     * @param request
     * @param userCaptcha
     * @return
     */
    public static boolean validate(HttpServletRequest request, String userCaptcha) {
        // 验证参数
        if (null == request || StringUtils.isEmpty(userCaptcha)) {
            return false;
        }

        // 获取session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        Object captcha = session.getAttribute(SESSION_KEY);
        if (captcha != null && userCaptcha.equalsIgnoreCase(captcha.toString())) {
            long timestamp = (Long) session.getAttribute(SESSION_TIME_KEY);
            // 判断是否过期
            if ((timestamp + TIMEOUT) > System.currentTimeMillis()) {
                session.removeAttribute(SESSION_KEY);
                session.removeAttribute(SESSION_TIME_KEY);
                return true;
            }
        }

        return false;
    }

    /**
     * <p>验证验证码是否正确</p>
     *
     * @param request
     * @return
     */
    public static boolean validate(HttpServletRequest request) {
        // 提交的验证码
        String userCaptcha = request.getParameter("captcha");
        return validate(request, userCaptcha);
    }
}
