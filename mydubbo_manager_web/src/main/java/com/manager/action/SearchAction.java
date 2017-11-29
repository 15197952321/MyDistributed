package com.manager.action;

import com.common.result.SCResult;
import com.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/11/21.
 */
@Controller
public class SearchAction {

    @Autowired
    private SearchItemService searchItemService;

    //将查询出的数据导入到索引库
    @RequestMapping("/index/item/import")
    @ResponseBody
    public SCResult findAllItemByInputSolr(){
        return searchItemService.findAllItemByInputSolr();
    }

}
