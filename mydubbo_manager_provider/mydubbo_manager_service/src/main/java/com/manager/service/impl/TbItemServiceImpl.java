package com.manager.service.impl;

import com.common.app.AppException;
import com.common.result.EasyUIDataGridResult;
import com.common.result.SCResult;
import com.common.utils.IDUtils;
import com.common.utils.JSONUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.manager.entity.TbItem;
import com.manager.entity.TbItemCat;
import com.manager.entity.TbItemDesc;
import com.manager.entity.TbItemExample;
import com.manager.mapper.TbItemDescMapper;
import com.manager.mapper.TbItemMapper;
import com.manager.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/11.
 */
@Service
public class TbItemServiceImpl implements TbItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private JedisCluster jedisCluster;

    //查询
    public EasyUIDataGridResult<TbItem> findAll(int page, int size){
        PageHelper.startPage(page, size);
        List<TbItem> list = tbItemMapper.selectByExample(null);
        PageInfo<TbItem> info = new PageInfo<>(list);
        EasyUIDataGridResult<TbItem> result = new EasyUIDataGridResult<>();
        result.setRows(list);
        result.setTotal(info.getTotal());

        return result;
    }

    //添加商品
    @Override
    public SCResult addItem(TbItem tbItem, String desc) {
        //设置属性
        tbItem.setStatus((byte)1);
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        long id = IDUtils.genItemId();
        tbItem.setId(id);
        //设置商品描述对象
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        itemDesc.setItemId(id);
        //添加
        try {
            int count = tbItemMapper.insert(tbItem);
            if(count <= 0){
                throw new AppException(201, "添加商品失败", null);
            }
            count = tbItemDescMapper.insert(itemDesc);
            if(count <= 0){
                throw new AppException(201, "添加商品详情失败", null);
            }
        }catch (AppException ex){
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new SCResult(ex.getStatus(), ex.getMsg(), ex.getData());
        }

        return new SCResult(200, "添加商品成功", id);
    }

    //根据ID查询商品描述信息
    @Override
    public SCResult findItemDescById(Long id) {
        //查询缓存中是否有数据
        TbItemDesc desc = new TbItemDesc();
        try{
            String str = jedisCluster.hget("Item", id + "_desc");
            if(str != null && !str.equals("")) {
                desc = JSONUtils.toBean(str, TbItemDesc.class);
                System.out.println("使用缓存了");
                return new SCResult(200, "", desc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        desc = tbItemDescMapper.selectByPrimaryKey(id);

        //添加到缓存
        try{
            jedisCluster.hset("Item", id + "_desc", JSONUtils.toObject(desc).toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return new SCResult(200, "", desc);
    }

    //根据ID查询商品信息
    @Override
    public TbItem findItemById(Long id) {
        //查询缓存中是否有数据
        TbItem item = new TbItem();
        try{
            String str = jedisCluster.hget("Item", id + "");
            if(!StringUtils.isEmpty(str)){
                item = JSONUtils.toBean(str, TbItem.class);
                System.out.println("-----缓存");
                return item;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        item = tbItemMapper.selectByPrimaryKey(id);

        //添加到缓存
        try{
            jedisCluster.hset("Item", id + "", JSONUtils.toObject(item).toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return item;
    }

    //修改商品信息
    @Override
    public SCResult updateItem(TbItem tbItem, String desc) {
        //修改商品
        TbItemExample ex = new TbItemExample();
        TbItemExample.Criteria ct = ex.createCriteria();
        ct.andIdEqualTo(tbItem.getId());
        ct.andUpdatedEqualTo(tbItem.getUpdated());
        //设置最新时间
        tbItem.setUpdated(new Date());
        try{
            int count = tbItemMapper.updateByExampleSelective(tbItem, ex);
            if(count <=0 ){
                throw new AppException(201, "修改商品信息失败", null);
            }
            TbItemDesc itemdesc = new TbItemDesc();
            itemdesc.setItemId(tbItem.getId());
            itemdesc.setItemDesc(desc);
            count = tbItemDescMapper.updateByPrimaryKeySelective(itemdesc);
            if(count <= 0){
                throw new AppException(201, "修改商品描述信息失败", null);
            }
        }catch (AppException e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new SCResult(e.getStatus(), e.getMsg(), e.getData());
        }

        return new SCResult(200, "修改商品成功", tbItem);
    }

    //删除商品信息
    @Override
    public SCResult deleteItem(Long[] ids) {
        try{
            int count = 0;
            for (Long id : ids) {
                count = tbItemDescMapper.deleteByPrimaryKey(id);
                if(count <= 0){
                    throw new AppException(201, "删除商品描述信息失败", null);
                }
                count = tbItemMapper.deleteByPrimaryKey(id);
                if(count <= 0){
                    throw new AppException(201, "删除商品信息失败", null);
                }
            }
        }catch (AppException e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new SCResult(e.getStatus(), e.getMsg(), e.getData());
        }
        return new SCResult(200, "删除商品信息成功", ids);
    }

    //上架
    @Override
    public SCResult up(Long[] ids) {
        int count = 0;
        TbItem item = new TbItem();
        for (Long id : ids) {
            item.setId(id);
            item.setStatus((byte)1);
            item.setUpdated(new Date());
            count = tbItemMapper.updateByPrimaryKeySelective(item);
            if(count <= 0){
                return new SCResult(201, "上架失败", null);
            }
        }
        return new SCResult(200, "上架成功", ids);
    }

    //下架
    @Override
    public SCResult down(Long[] ids) {
        int count = 0;
        TbItem item = new TbItem();
        for (Long id : ids) {
            item.setId(id);
            item.setStatus((byte)2);
            item.setUpdated(new Date());
            count = tbItemMapper.updateByPrimaryKeySelective(item);
            if(count <= 0){
                return new SCResult(201, "下架失败", null);
            }
        }
        return new SCResult(200, "下架成功", ids);
    }

}
