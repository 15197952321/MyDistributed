package com.content.service;

import com.common.result.EasyUIDataGridResult;
import com.common.result.SCResult;
import com.content.entity.TbContent;
import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */
public interface TbContentService {

    //查询
    EasyUIDataGridResult<TbContent> findAll(Long categoryId, int page, int rows);

    //添加
    SCResult addTbContent(TbContent tbContent);

    //修改
    SCResult updateTbContent(TbContent tbContent);

    //删除
    SCResult deleteTbContent(Long[] ids, Long cid);

    //根据种类ID查询
    List<TbContent> findByCategoryId(Long categoryId);

}
