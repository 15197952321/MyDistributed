package com.common.app;

import com.common.result.SCResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/11/14.
 */
public class BaseAction {

    @ExceptionHandler
    @ResponseBody
    public SCResult doException(Exception ex){
        ex.printStackTrace();
        return new SCResult(500, "系统异常", null);
    }

}
