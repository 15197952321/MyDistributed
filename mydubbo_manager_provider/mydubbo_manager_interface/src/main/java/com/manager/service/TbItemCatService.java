package com.manager.service;

import com.common.result.EasyUITreeNode;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */
public interface TbItemCatService {

    //根据parentID查询
    List<EasyUITreeNode> findAllByPid(Long parentId);

}
