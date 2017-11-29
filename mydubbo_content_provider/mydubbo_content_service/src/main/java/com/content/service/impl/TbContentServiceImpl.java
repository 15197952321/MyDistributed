package com.content.service.impl;

import com.common.result.EasyUIDataGridResult;
import com.common.result.SCResult;
import com.common.utils.JSONUtils;
import com.content.entity.TbContent;
import com.content.entity.TbContentExample;
import com.content.mapper.TbContentMapper;
import com.content.service.TbContentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */
@Service
public class TbContentServiceImpl implements TbContentService {

    @Autowired
    private TbContentMapper tbContentMapper;
    @Autowired
    private JedisCommands jedis;
    @Value("{CONTENT_CAT}")
    private String CONTENT_CAT;

    @Override
    public EasyUIDataGridResult<TbContent> findAll(Long categoryId, int page, int rows) {
        //开启分页
        PageHelper.startPage(page, rows);
        //查询
        TbContentExample ex = new TbContentExample();
        TbContentExample.Criteria ct = ex.createCriteria();
        ct.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExample(ex);
        PageInfo<TbContent> info = new PageInfo<>(list);
        EasyUIDataGridResult<TbContent> result = new EasyUIDataGridResult<>();
        result.setRows(info.getList());
        result.setTotal(info.getTotal());

        return result;
    }

    //添加
    @Override
    public SCResult addTbContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        int count = tbContentMapper.insert(tbContent);
        if(count <= 0){
            return new SCResult(201, "添加内容失败", null);
        }

        //同步缓存
        try{
            jedis.hdel(CONTENT_CAT, tbContent.getCategoryId() + "");
        }catch (Exception e){
            e.printStackTrace();
        }

        return new SCResult(200, "添加内容成功", tbContent);
    }

    //修改
    @Override
    public SCResult updateTbContent(TbContent tbContent) {
        Date date = new Date();
        TbContentExample ex = new TbContentExample();
        TbContentExample.Criteria ct = ex.createCriteria();
        ct.andIdEqualTo(tbContent.getId());
        ct.andUpdatedEqualTo(tbContent.getUpdated());
        tbContent.setUpdated(date);
        int count = tbContentMapper.updateByExampleSelective(tbContent, ex);
        if(count <= 0){
            return new SCResult(201, "修改内容失败", null);
        }

        //同步缓存
        try{
            jedis.hdel(CONTENT_CAT, tbContent.getCategoryId() + "");
        }catch (Exception e){
            e.printStackTrace();
        }

        return new SCResult(200, "修改内容成功", tbContent);
    }

    //删除
    @Override
    public SCResult deleteTbContent(Long[] ids, Long cid) {
        int count = 0;
        for (Long id : ids) {
            count = tbContentMapper.deleteByPrimaryKey(id);
            if(count <= 0){
                return new SCResult(201, "删除内容失败", null);
            }
        }

        //同步缓存
        try{
            jedis.hdel(CONTENT_CAT, cid + "");
        }catch (Exception e){
            e.printStackTrace();
        }

        return new SCResult(200, "删除内容成功", null);
    }

    //根据种类ID查询
    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {
        //从缓存拿取数据
        try{
            String jstr = jedis.hget(CONTENT_CAT, categoryId + "");
            if(!StringUtils.isEmpty(jstr)){
                System.out.println("使用缓存了");
                return JSONUtils.toList(jstr, TbContent.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        TbContentExample ex = new TbContentExample();
        TbContentExample.Criteria ct = ex.createCriteria();
        ct.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExample(ex);

        //保存到缓存
        try{
            jedis.hset(CONTENT_CAT, categoryId + "", JSONUtils.toJSONString(list));
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }


}
