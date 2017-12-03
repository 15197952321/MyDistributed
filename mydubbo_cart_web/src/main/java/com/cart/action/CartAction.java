package com.cart.action;

import com.cart.entity.TbItem;
import com.cart.service.CartService;
import com.common.result.SCResult;
import java.util.*;
import com.user.entity.TbUser;
import com.user.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/12/2.
 */
@Controller
public class CartAction {

    @Autowired
    private CartService cartService;
    @Autowired
    private TbUserService userService;

    //获取当前登录用户信息
    private TbUser getUser(HttpServletRequest request){
        //获取cookie
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
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

        return user;
    }

    //添加购物车
    @RequestMapping("/cart/add/{pid}/{num}")
    public String addCart(@PathVariable("pid") long itemId, @PathVariable("num") int num, HttpServletRequest request){
        //获取用户信息
        //TbUser user = getUser(request);
        TbUser user = (TbUser) request.getAttribute("user");
        if(user == null){
            return "carterror";
        }
        cartService.addCart(user.getId(), itemId, num);

        return "cartSuccess";
    }

    //查询
    @RequestMapping("/cart/cart")
    public String findCart(HttpServletRequest request){
        //TbUser user = getUser(request);
        TbUser user = (TbUser) request.getAttribute("user");
        if(user == null){
            return "carterror";
        }
        //查询
        List<TbItem> items = cartService.findItem(user.getId());
        request.setAttribute("cartList", items);

        return "cart";
    }

    //清空购物车
    @RequestMapping("/cart/delall")
    public String delCartAll(HttpServletRequest request){
        TbUser user = (TbUser) request.getAttribute("user");
        if(user == null){
            return "carterror";
        }
        //删除redis的数据
        cartService.delCartAll(user.getId());

        return "cart";
    }

    //单个和批量删除
    @RequestMapping("/cart/delete/{itemid}")
    public String delCart(HttpServletRequest request, @PathVariable("itemid") long[] itemid){
        TbUser user = (TbUser) request.getAttribute("user");
        if(user == null){
            return "carterror";
        }
        //
        cartService.delCart(user.getId(), itemid);

        return "forward:/cart/cart";
    }

}
