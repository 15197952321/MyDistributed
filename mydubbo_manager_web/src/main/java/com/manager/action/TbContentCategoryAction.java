package com.manager.action;

import com.common.result.EasyUITreeNode;
import com.common.result.SCResult;
import com.content.entity.TbContentCategory;
import com.content.service.TbContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2017/11/19.
 */
@Controller
public class TbContentCategoryAction {

    @Autowired
    private TbContentCategoryService tbContentCategoryService;

    //查询
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> findTbContentCategory(@RequestParam(name = "id", defaultValue = "0") Long parentId){
        return tbContentCategoryService.findTbContentCategory(parentId);
    }

    //添加
    @RequestMapping("/content/category/create")
    @ResponseBody
    public SCResult addTbContentCategory(TbContentCategory category){
        return tbContentCategoryService.addTbContentCategory(category);
    }

    //修改
    @RequestMapping("/content/category/update")
    @ResponseBody
    public SCResult updateTbContentCategory(TbContentCategory category){
        return tbContentCategoryService.updateTbContentCategory(category);
    }

    //删除
    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public SCResult deleteTbContentCategory(Long id){
        return tbContentCategoryService.deleteTbContentCategory(id);
    }

}
