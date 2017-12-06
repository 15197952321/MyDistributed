package com.pay.action;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.common.result.SCResult;
import com.pay.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/5.
 */
@Controller
public class PayAction {

    private static final Logger log = LoggerFactory.getLogger(PayAction.class);

    @Autowired
    private PayService payService;

    @RequestMapping("/pay/create")
    public String create(String orderNo, Model model){
        SCResult result = payService.createOrder(orderNo);
        model.addAttribute("result", result);

        return "pay";
    }

    @RequestMapping("/pay/callback")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String,String> params = new HashMap<>();
        System.out.println("---------:" + request.getParameter("trade_status"));
        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){
                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }
        //System.out.println(params.get("sign") + ":" + params.get("trade_status") + ":" + params.toString());
        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8", Configs.getSignType());

            if(!alipayRSACheckedV2){
                return "非法请求,验证不通过,再恶意请求我就报警找网警了";
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常",e);
        }

        SCResult result = payService.aliCallback(params);
        if(result.getStatus()==200){
            return "success";
        }
        return "failed";
    }

}
