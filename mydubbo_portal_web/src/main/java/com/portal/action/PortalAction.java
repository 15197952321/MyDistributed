package com.portal.action;

import com.content.entity.TbContent;
import com.content.service.TbContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */
@Controller
public class PortalAction {

    @Autowired
    private TbContentService tbContentService;
    @Value("${CONTENT_LUNBO_ID}")
    private Long CONTENT_LUNBO_ID;

    @RequestMapping("/")
    public ModelAndView toIndex(){
        ModelAndView view = new ModelAndView();
        List<TbContent> list = tbContentService.findByCategoryId(CONTENT_LUNBO_ID);
        view.addObject("ad1List", list);
        view.setViewName("index");

        return view;
    }

}
