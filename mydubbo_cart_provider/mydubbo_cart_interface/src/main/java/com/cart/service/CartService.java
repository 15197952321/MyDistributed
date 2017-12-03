package com.cart.service;

import com.cart.entity.TbItem;
import com.common.result.SCResult;
import java.util.*;

/**
 * Created by Administrator on 2017/12/2.
 */
public interface CartService {

    //添加
    SCResult addCart(long uid, long itemid, int num);

    //查询
    List<TbItem> findItem(long uid);

    //清空购物车
    void delCartAll(long uid);

    //单个和批量删除
    void delCart(long uid, long[] itemid);

}
