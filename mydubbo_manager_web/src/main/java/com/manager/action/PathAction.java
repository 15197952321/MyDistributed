package com.manager.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/11/13.
 */
@Controller
public class PathAction {

    @RequestMapping("/{path}")
    public String toPage(@PathVariable("path")String path){
        return path;
    }

}
