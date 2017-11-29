package com.search.service.mqlistener;

import com.common.utils.ArrayUtils;
import com.search.service.SearchItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by Administrator on 2017/11/25.
 */
public class DeleteSolrMqListener implements MessageListener {

    private static Logger log = LoggerFactory.getLogger(DeleteSolrMqListener.class);

    @Autowired
    private SearchItemService searchItemService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try{
            Object[] obs = ArrayUtils.stringToArray(textMessage.getText());
            for (Object ob : obs) {
                searchItemService.deleteSolr(Long.parseLong(ob.toString()));
            }
            //
            log.info("删除Solr数据成功");
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除Solr数据失败", e);
        }
    }

}
