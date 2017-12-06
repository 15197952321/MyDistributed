package com.pay.service;

import com.common.result.SCResult;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/5.
 */
public interface PayService {

    //创建订单
    SCResult createOrder(String orderno);

    //回调修改订单状态
    SCResult aliCallback(Map<String, String> params);

}
