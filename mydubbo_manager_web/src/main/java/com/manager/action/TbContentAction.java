package com.manager.action;

import com.common.result.EasyUIDataGridResult;
import com.common.result.SCResult;
import com.content.entity.TbContent;
import com.content.service.TbContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/11/20.
 */
@Controller
public class TbContentAction {

    @Autowired
    private TbContentService tbContentService;

    //查询
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult<TbContent> findAll(Long categoryId, int page, int rows){
        return tbContentService.findAll(categoryId, page, rows);
    }

    //添加
    @RequestMapping("/content/save")
    @ResponseBody
    public SCResult addTbContent(TbContent tbContent){
        return tbContentService.addTbContent(tbContent);
    }

    //修改
    @RequestMapping("/content/edit")
    @ResponseBody
    public SCResult updateTbContent(TbContent tbContent){
        return tbContentService.updateTbContent(tbContent);
    }

    //删除
    @RequestMapping("/content/delete")
    @ResponseBody
    public SCResult deleteTbContent(Long[] ids, Long cid){
        return tbContentService.deleteTbContent(ids, cid);
    }

}
