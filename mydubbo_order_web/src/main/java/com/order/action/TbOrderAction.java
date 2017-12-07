package com.order.action;

import com.cart.entity.TbItem;
import com.cart.service.CartService;
import com.common.result.SCResult;
import com.order.entity.TbOrder;
import com.order.service.TbOrderService;
import com.user.entity.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2017/12/7.
 */
@Controller
public class TbOrderAction {

    @Autowired
    private TbOrderService orderService;
    @Autowired
    private CartService cartService;

    //生成确认订单页面
    @RequestMapping("/order/order-cart")
    public String toConfirmOrder(HttpServletRequest request){
        //获取用户 信息
        TbUser user = (TbUser) request.getAttribute("user");
        //
        List<TbItem> list = cartService.findItem(user.getId());
        request.setAttribute("cartList", list);

        return "order-cart";
    }

    //添加订单
    @RequestMapping("/order/create")
    public String createOrder(TbOrder order, HttpServletRequest request){
        //获取用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        //设置值
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        SCResult result = orderService.createOrder(order);
        if(result.getStatus() == 200){
            //清空购物车
            cartService.delCartAll(user.getId());
        }
        //
        request.setAttribute("payment", order.getPayment());
        request.setAttribute("orderId", result.getData());

        return "success";
    }


}
