package com.item.mqlistener;

import com.common.result.SCResult;
import com.manager.entity.TbItem;
import com.manager.entity.TbItemDesc;
import com.manager.service.TbItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/28.
 */
public class AddOrUpdateItemListener implements MessageListener {

    private static Logger log = LoggerFactory.getLogger(AddOrUpdateItemListener.class);

    @Autowired
    private TbItemService tbItemService;
    @Autowired
    private FreeMarkerConfigurer configurer;

    @Override
    public void onMessage(Message message) {
        try{
            TextMessage textMessage = (TextMessage) message;
            long id = Long.parseLong(textMessage.getText());
            //查询数据
            TbItem tbItem = tbItemService.findItemById(id);
            SCResult result = tbItemService.findItemDescById(id);
            TbItemDesc itemDesc = (TbItemDesc) result.getData();
            //创建读取freemarker配置对象
            Configuration config = configurer.getConfiguration();
            //创建模板对象
            Template template = config.getTemplate("item.ftl");
            //保存数据
            Map<String, Object> map = new HashMap<>();
            map.put("item", tbItem);
            map.put("itemDesc", itemDesc);
            //创建输出流
            Writer out = new FileWriter("E:/tomcat/apache-tomcat-7.0.79/webapps/item/" + id + ".html");
            //生成
            template.process(map, out);
            //关闭
            out.close();

            //打印日志
            log.info("生成静态页面成功");

        }catch (Exception e){
            e.printStackTrace();
            log.error("生成静态页面失败", e);
        }

    }

}

