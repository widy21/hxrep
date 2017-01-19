package com.hx.med.common.filters;

import com.hx.med.sys.entity.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by huaxiao on 2017/1/19.
 */
public class SecurityFilter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        System.out.println("==>>Begin to Filter session====");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        if (user != null) {
            return true;
        } else {
            /**
             * handle session and security if you want.
             */
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
        return super.preHandle(request, response, handler);
    }
}
