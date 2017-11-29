package com.manager.service.impl;

import com.common.result.EasyUITreeNode;
import com.manager.entity.TbItemCat;
import com.manager.entity.TbItemCatExample;
import com.manager.mapper.TbItemCatMapper;
import com.manager.service.TbItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */
@Service
public class TbItemCatServiceImpl implements TbItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    //根据parentID查询
    @Override
    public List<EasyUITreeNode> findAllByPid(Long parentId) {
        //查询
        List<EasyUITreeNode> nodes = new ArrayList<>();
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria ct = example.createCriteria();
        ct.andParentIdEqualTo(parentId);
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
        //遍历赋值
        for (TbItemCat tbItemCat : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            if(tbItemCat.getIsParent()){
                node.setState("closed");
            }else{
                node.setState("open");
            }
            nodes.add(node);
        }

        return nodes;
    }
}
