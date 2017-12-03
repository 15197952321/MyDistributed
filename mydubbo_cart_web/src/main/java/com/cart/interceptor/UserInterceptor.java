package com.cart.interceptor;

import com.user.entity.TbUser;
import com.user.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/12/3.
 */
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private TbUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //获取cookie
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return true;
        }
        ///遍历
        String token = "";
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")){
                token = cookie.getValue();
                break;
            }
        }
        //从redis中查询用户信息
        TbUser user = (TbUser) userService.toRedisByCookie(token).getData();
        //放入域中
        request.setAttribute("user", user);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
