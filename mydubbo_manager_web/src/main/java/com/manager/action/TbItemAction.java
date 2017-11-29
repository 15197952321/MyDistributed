package com.manager.action;

import com.common.app.BaseAction;
import com.common.result.EasyUIDataGridResult;
import com.common.result.SCResult;
import com.common.utils.ArrayUtils;
import com.manager.entity.TbItem;
import com.manager.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/11.
 */
@Controller
public class TbItemAction extends BaseAction {

    @Autowired
    private TbItemService tbItemService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "topicDestinationAddOrUpdate")
    private Destination destinationAddOrUpdate;
    @Resource(name = "topicDestinationDelete")
    private Destination destinationDelete;

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult<TbItem> findAll(int page, int rows){
        return tbItemService.findAll(page, rows);
    }

    //添加商品
    @RequestMapping("/item/save")
    @ResponseBody
    public SCResult addItem(TbItem item, String desc){
        final SCResult scResult = tbItemService.addItem(item, desc);
        if(scResult.getStatus()==200){
            jmsTemplate.send(destinationAddOrUpdate, new MessageCreator(){
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage(scResult.getData().toString());
                    return textMessage;
                }
            });
        }

        return scResult;
    }

    //根据ID查询描述信息
    @RequestMapping("/item/desc/{id}")
    @ResponseBody
    public SCResult findItemDescById(@PathVariable("id")Long id){
        return tbItemService.findItemDescById(id);
    }

    //修改商品信息
    @RequestMapping("/item/update")
    @ResponseBody
    public SCResult updateItem(final TbItem tbItem, String desc){
        final SCResult result = tbItemService.updateItem(tbItem, desc);
        if(result.getStatus()==200){
            jmsTemplate.send(destinationAddOrUpdate, new MessageCreator(){
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage(tbItem.getId().toString());
                    return textMessage;
                }
            });
        }

        return result;
    }

    //删除商品信息
    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public SCResult deleteItem(final Long[] ids){
        SCResult result = tbItemService.deleteItem(ids);
        if(result.getStatus()==200){
            jmsTemplate.send(destinationDelete, new MessageCreator(){
                public Message createMessage(Session session) throws JMSException {
                    String strs = ArrayUtils.arrayToString(ids);
                    TextMessage textMessage = session.createTextMessage(strs);
                    return textMessage;
                }
            });
        }

        return result;
    }

    //上架商品
    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public SCResult upItem(Long[] ids){
        return tbItemService.up(ids);
    }

    //下架商品
    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public SCResult downItem(Long[] ids){
        return tbItemService.down(ids);
    }

}