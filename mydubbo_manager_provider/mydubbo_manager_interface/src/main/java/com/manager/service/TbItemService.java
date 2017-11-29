package com.manager.service;

import com.common.result.EasyUIDataGridResult;
import com.common.result.SCResult;
import com.manager.entity.TbItem;

import java.util.List;

/**
 * Created by Administrator on 2017/11/11.
 */
public interface TbItemService {

    //查询
    EasyUIDataGridResult<TbItem> findAll(int page, int size);

    //添加商品
    SCResult addItem(TbItem tbItem, String desc);

    //根据ID查询商品描述信息
    SCResult findItemDescById(Long id);

    //根据ID查询商品信息
    TbItem findItemById(Long id);

    //修改商品信息
    SCResult updateItem(TbItem tbItem, String desc);

    //删除商品信息
    SCResult deleteItem(Long[] ids);

    //上架商品
    SCResult up(Long[] ids);

    //下架商品
    SCResult down(Long[] ids);

}
