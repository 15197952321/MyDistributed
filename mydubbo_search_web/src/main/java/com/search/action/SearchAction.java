package com.search.action;

import com.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/22.
 */
@Controller
public class SearchAction {

    @Autowired
    private SearchItemService searchItemService;
    @Value("${ROWS}")
    private Integer rows;

    @RequestMapping("/search")
    public String selectSolr(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws SolrServerException {
        Map<String, Object> map = searchItemService.selectSolr(keyword, page, rows);
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", map.get("totalPages"));
        model.addAttribute("page", page);
        model.addAttribute("recourdCount", map.get("recourdCount"));
        model.addAttribute("itemList", map.get("itemList"));

        return "search";
    }

}
