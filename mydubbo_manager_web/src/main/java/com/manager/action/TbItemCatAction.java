package com.manager.action;

import com.common.result.EasyUITreeNode;
import com.manager.service.TbItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */
@Controller
public class TbItemCatAction {

    @Autowired
    private TbItemCatService tbItemCatService;

    //根据parentID查询
    @RequestMapping("item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> findAllByPid(@RequestParam(name = "id", defaultValue = "0") Long parenId){
        return tbItemCatService.findAllByPid(parenId);
    }

}
