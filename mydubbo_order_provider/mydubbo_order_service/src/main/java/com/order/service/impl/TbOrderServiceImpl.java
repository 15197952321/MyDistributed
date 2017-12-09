package com.order.service.impl;

import com.common.app.AppException;
import com.common.result.SCResult;
import com.order.entity.TbOrder;
import com.order.entity.TbOrderItem;
import com.order.mapper.TbOrderItemMapper;
import com.order.mapper.TbOrderMapper;
import com.order.service.TbOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCommands;

import java.util.*;

/**
 * Created by Administrator on 2017/12/7.
 */
@Service
public class TbOrderServiceImpl implements TbOrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private JedisCommands jedis;
    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;
    @Value("${ORDER_ID_START}")
    private String ORDER_ID_START;
    @Value("${ORDER_DETAIL_ID_GEN_KEY}")
    private String ORDER_DETAIL_ID_GEN;

    @Override
    public SCResult createOrder(TbOrder order) {
        //判断是否为第一笔订单
        if (!jedis.exists(ORDER_ID_GEN_KEY)) {
            jedis.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
        }
        //子增长
        String orderId = jedis.incr(ORDER_ID_GEN_KEY).toString();
        //设置值
        order.setOrderId(orderId);
        order.setStatus(1);
        Date date = new Date();
        order.setCreateTime(date);
        order.setUpdateTime(date);
        try {
            //添加订单表
            int count = orderMapper.insert(order);
            if(count <= 0){
                throw new AppException(201, "添加订单表失败", null);
            }
            //添加订单明细
            List<TbOrderItem> itemList = order.getOrderItems();
            for (TbOrderItem tbOrderItem : itemList) {
                //设置ID
                tbOrderItem.setId(jedis.incr(ORDER_DETAIL_ID_GEN).toString());
                tbOrderItem.setOrderId(orderId);
                //
                count = orderItemMapper.insert(tbOrderItem);
                if(count <= 0){
                    throw new AppException(201, "添加订单明细失败", null);
                }
            }

        }catch (AppException e){
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new SCResult(e.getStatus(), e.getMsg(), e.getData());
        }

        return new SCResult(200, "添加订单成功", orderId);
    }

    //
    @Override
    public void testTask() {
        System.out.println("任务调度开启");
    }
}
