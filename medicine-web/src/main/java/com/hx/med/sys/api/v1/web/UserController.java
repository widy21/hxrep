package com.hx.med.sys.api.v1.web;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.hx.med.sys.exception.BusinessException;
import com.hx.med.sys.service.interfaces.UserService;
import com.hx.med.sys.vo.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by huaxiao on 2016/12/16.
 *
 * @author huaxiao
 * @since 1.0
 */
@Controller
@RequestMapping(value = "/med")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService userService;

    /**
     * 验证登录用户信息.
     *
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object sendEmailOrPhoneCode(@RequestBody @FluentValid final LoginForm loginForm,HttpServletRequest request) throws BusinessException {
        Map resultMap = userService.checkLoginUser(loginForm);
        if(resultMap.get("loginFlag").equals("true")){
            request.getSession().setAttribute("loginUser", resultMap.get("loginUser"));
        }
        return resultMap;
    }

    @RequestMapping(value = "/showloginpage", method = RequestMethod.GET)
    public Object showLoginPage() throws BusinessException {
        return "login";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public Object showMainPage(HttpServletRequest request) throws BusinessException {
        String username = request.getParameter("username");
        //session校验 TODO
        return "main";
    }

    /*@ResponseBody
    @RequestMapping(value = "/t2", method = RequestMethod.GET)
    public Object updateUser() throws BusinessException {
        Map resultMap = userService.update();
        return resultMap;
    }*/
}
