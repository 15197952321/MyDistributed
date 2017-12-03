package com.cart.service.impl;

import com.cart.entity.TbItem;
import com.cart.mapper.TbItemMapper;
import com.cart.service.CartService;
import com.common.result.SCResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/2.
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private JedisCommands jedis;

    //添加购物车
    @Override
    public SCResult addCart(long uid, long itemid, int num) {
        //从redis中查询此用户是否购买过此商品
        boolean flag = jedis.hexists("cart_" + uid, itemid + "");
        if(flag){
            long num1 = Long.parseLong(jedis.hget("cart_" + uid, itemid + ""));
            num += num1;
        }
        //写入redis
        jedis.hset("cart_" + uid, itemid + "", num + "");

        return new SCResult(200, "添加到购物车成功", null);
    }

    //查询
    @Override
    public List<TbItem> findItem(long uid) {
        List<TbItem> items = new ArrayList<>();
        Set<String> set = jedis.hkeys("cart_" + uid);
        for (String s : set) {
            long itemId = Long.parseLong(s);
            //查询
            TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
            item.setImage(item.getImages()[0]);
            int num = Integer.parseInt(jedis.hget("cart_" + uid, s));
            item.setNum(num);
            items.add(item);
        }
        return items;
    }

    //清空购物车
    @Override
    public void delCartAll(long uid) {
        Set<String> set = jedis.hkeys("cart_" + uid);
        for (String s : set) {
            jedis.hdel("cart_" + uid, s);
        }
    }

    //单个和批量删除
    @Override
    public void delCart(long uid, long[] itemid) {
        for (long l : itemid) {
            jedis.hdel("cart_" + uid, l + "");
        }
    }


}
