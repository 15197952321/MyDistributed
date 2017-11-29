package com.search.service.mqlistener;

import com.search.service.SearchItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by Administrator on 2017/11/24.
 */
public class AddOrUpdateSolrMqListener implements MessageListener {

    private static Logger log = LoggerFactory.getLogger(AddOrUpdateSolrMqListener.class);

    @Autowired
    private SearchItemService searchItemService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            long id = Long.parseLong(textMessage.getText());
            searchItemService.addOrUpdateSolr(id);
            //打印日志
            log.info("添加或修改数据到Solr成功: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加或修改数据到Solr失败", e);
        }
    }

}
