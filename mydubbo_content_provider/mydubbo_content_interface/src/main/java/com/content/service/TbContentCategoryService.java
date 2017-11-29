package com.content.service;

import com.common.result.EasyUITreeNode;
import com.common.result.SCResult;
import com.content.entity.TbContentCategory;

import java.util.List;

/**
 * Created by Administrator on 2017/11/19.
 */
public interface TbContentCategoryService {

    //查询
    List<EasyUITreeNode> findTbContentCategory(Long parentId);

    //添加
    SCResult addTbContentCategory(TbContentCategory category);

    //修改
    SCResult updateTbContentCategory(TbContentCategory category);

    //删除
    SCResult deleteTbContentCategory(Long id);

}
