package com.item.action;

import com.common.result.SCResult;
import com.manager.entity.TbItem;
import com.manager.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/11/27.
 */
@Controller
public class ItemAction {

    @Autowired
    private TbItemService tbItemService;

    @RequestMapping("/item/{itemid}")
    public String findItemById(@PathVariable("itemid") Long id, Model model){
        //查询商品信息
        TbItem item = tbItemService.findItemById(id);
        //查询商品描述信息
        SCResult result = tbItemService.findItemDescById(id);

        //
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", result.getData());

        return "item";
    }

}
