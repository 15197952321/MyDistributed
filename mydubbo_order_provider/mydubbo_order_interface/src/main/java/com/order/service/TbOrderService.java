package com.order.service;

import com.common.result.SCResult;
import com.order.entity.TbOrder;

/**
 * Created by Administrator on 2017/12/7.
 */
public interface TbOrderService {

    //从redis查询数据
    SCResult createOrder(TbOrder order);

}
