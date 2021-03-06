package com.damon.rmq.admin.web;

import cn.hutool.crypto.SecureUtil;
import com.damon.rmq.admin.utils.CaptchaValidateUtil;
import com.damon.rmq.api.admin.model.po.SysUser;
import com.damon.rmq.api.admin.service.ISysUserService;
import com.damon.rmq.api.model.Constants;
import com.damon.rmq.api.model.dto.RspBase;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @DubboReference
    private ISysUserService sysUserService;

    /**
     * <p>首页</p>
     *
     * @return 返回首页
     */
    @RequestMapping(value = "index")
    public String index(HttpServletRequest request) {
        Object user = request.getSession().getAttribute(Constants.SESSION_USER);
        if (null == user) {
            return "redirect:/login";
        }
        return "index";
    }

    /**
     * <p>登录页面</p>
     *
     * @return 返回登陆页面
     */
    @RequestMapping(value = "login")
    public String login() {
        return "login";
    }

    /**
     * <p>用户登录</p>
     */
    @RequestMapping(value = "login/submit", method = RequestMethod.POST)
    @ResponseBody
    public Object submit(HttpServletRequest request, @RequestParam("username") String username,
                         @RequestParam("password") String password, @RequestParam("captcha") String captcha) {

        RspBase<Void> rspBase = new RspBase<>();
        if (!CaptchaValidateUtil.validate(request, captcha)) {
            return rspBase.code(Constants.CODE_FAILURE).msg("验证码错误");
        }

        SysUser user = sysUserService.selectByUserNameAndPassWord(username, SecureUtil.md5(password));
        if (null == user) {
            return rspBase.code(Constants.CODE_FAILURE).msg("用户名或密码错误").data(null);
        } else {
            if (user.getUserStatus() != null && user.getUserStatus() == 0) {
                return rspBase.code(Constants.CODE_FAILURE).msg("该用户已被禁用");
            }
            request.getSession().setAttribute(Constants.SESSION_USER, user);
            UsernamePasswordToken token = new UsernamePasswordToken(username, SecureUtil.md5(password));
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            token.setRememberMe(true);
            return null;
        }
    }

    /**
     * <p>用户登出</p>
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
