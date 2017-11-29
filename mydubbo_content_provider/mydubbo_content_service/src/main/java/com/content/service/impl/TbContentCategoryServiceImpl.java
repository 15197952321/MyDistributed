package com.content.service.impl;

import com.common.app.AppException;
import com.common.result.EasyUITreeNode;
import com.common.result.SCResult;
import com.content.entity.TbContentCategory;
import com.content.entity.TbContentCategoryExample;
import com.content.mapper.TbContentCategoryMapper;
import com.content.service.TbContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/19.
 */
@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> findTbContentCategory(Long parentId) {
        //查询数据
        TbContentCategoryExample ex = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria ct = ex.createCriteria();
        ct.andParentIdEqualTo(parentId);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(ex);
        //遍历赋值
        List<EasyUITreeNode> nodes = new ArrayList<>();
        for (TbContentCategory tbContentCategory : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            if(tbContentCategory.getIsParent()){
                node.setState("closed");
            }else{
                node.setState("open");
            }
            nodes.add(node);
        }

        return nodes;
    }

    //添加
    @Override
    public SCResult addTbContentCategory(TbContentCategory category) {
        //把父节点修改成父节点状态
        try {
            TbContentCategory c = new TbContentCategory();
            c.setId(category.getParentId());
            c.setIsParent(true);
            int count = tbContentCategoryMapper.updateByPrimaryKeySelective(c);
            if (count <= 0) {
                throw new AppException(201, "修改父节点失败", null);
            }
            //设置值
            Date date = new Date();
            category.setUpdated(date);
            category.setCreated(date);
            category.setIsParent(false);
            category.setStatus(1);
            category.setSortOrder(1);
            //添加
            count = tbContentCategoryMapper.insert(category);
            if (count <= 0) {
                throw new AppException(201, "添加节点失败", null);
            }
        }catch (AppException e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new SCResult(e.getStatus(), e.getMsg(), e.getData());
        }

        return new SCResult(200, "添加节点成功", category);
    }

    //修改
    @Override
    public SCResult updateTbContentCategory(TbContentCategory category) {
        category.setUpdated(new Date());
        int count = tbContentCategoryMapper.updateByPrimaryKeySelective(category);
        if(count <= 0){
            return new SCResult(201, "修改节点失败", null);
        }
        return new SCResult(200, "修改节点成功", category);
    }

    //删除
    @Override
    public SCResult deleteTbContentCategory(Long id) {
        List<Long> list = new ArrayList<>();
        list.add(id);
        saveId(id, list);
        //遍历删除
        int count = 0;
        for (Long aLong : list) {
            count = tbContentCategoryMapper.deleteByPrimaryKey(aLong);
            if(count <= 0){
                return new SCResult(201, "删除节点失败", null);
            }
        }
        return new SCResult(200, "删除节点成功", null);
    }

    //递归删除
    private void saveId(Long id, List<Long> list){
        //查询是否有字节点
        TbContentCategoryExample ex = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria ct = ex.createCriteria();
        ct.andParentIdEqualTo(id);
        List<TbContentCategory> categorys =tbContentCategoryMapper.selectByExample(ex);
        if(categorys!=null && categorys.size()>=0) {
            for (TbContentCategory category : categorys) {
                list.add(category.getId());
                saveId(category.getId(), list);
            }
        }
    }


}
