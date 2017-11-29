package com.manager.action;

import com.common.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/13.
 */
@Controller
public class FileAction {

    @Autowired
    private FastDFSClient client;
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping("pic/upload")
    @ResponseBody
    public Map fileUpLoad(MultipartFile uploadFile){
        System.out.println("进入图片上传方法" + "--" + IMAGE_SERVER_URL);
        Map map = new HashMap();
        //截取后缀名
        String exp = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".")+1);
        try {
            String path = client.uploadFile(uploadFile.getBytes(), exp, null);
            map.put("error", 0);
            map.put("url", IMAGE_SERVER_URL + path);
            System.out.println(path);
        } catch (Exception e) {
            map.put("error", 1);
            map.put("message", "文件上传失败");
        }
        return map;
    }

}
