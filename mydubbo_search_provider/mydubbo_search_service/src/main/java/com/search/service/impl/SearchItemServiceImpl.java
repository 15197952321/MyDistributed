package com.search.service.impl;

import com.common.result.SCResult;
import com.common.result.SearchItem;
import com.search.mapper.TbItemMapper;
import com.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2017/11/21.
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public SCResult findAllItemByInputSolr() {
        try{
            //查询数据
            List<SearchItem> list = tbItemMapper.findAllItmeByInputSolr();
            for (SearchItem searchItem : list) {
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                //添加域值
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategory_name());
                document.addField("item_desc", searchItem.getItem_desc());

                solrServer.add(document);
            }
            //提交
            solrServer.commit();

            return new SCResult(200, "数据导入成功", null);
        }catch (Exception e){
            e.printStackTrace();
            return new SCResult(201, "数据导入出现异常", null);
        }
    }

    //从solr查询
    @Override
    public Map<String, Object> selectSolr(String keyWord, Integer page, Integer rows) throws SolrServerException {
        Map<String, Object> map = new HashMap<>();
        //创建查询条件对象
        SolrQuery query = new SolrQuery();
        //设置条件
        query.set("df", "item_keywords");
        query.set("q", keyWord);
        query.setStart((page - 1) * rows);
        query.setRows(rows);
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style='color:red'>");
        query.setHighlightSimplePost("</em>");

        //获取查询对象
        QueryResponse response = solrServer.query(query);
        //查询普通数据
        SolrDocumentList documentList = response.getResults();
        //查询高亮数据
        Map<String, Map<String, List<String>>> resultMap = response.getHighlighting();
        //遍历赋值
        List<SearchItem> items = new ArrayList<>();
        SearchItem item = new SearchItem();
        for (SolrDocument entries : documentList) {
            item.setId(entries.get("id").toString());
            Object title = resultMap.get(entries.get("id")).get("item_title");
            if(title!=null){
                item.setTitle(title.toString());
            }else{
                item.setTitle(entries.get("item_title").toString());
            }
            item.setPrice((long)entries.get("item_price"));
            item.setImage(entries.get("item_image").toString());

            items.add(item);
        }
        //添加
        map.put("itemList", items);
        map.put("recourdCount", documentList.getNumFound());
        map.put("totalPages", (documentList.getNumFound() + rows - 1) / rows);

        return map;
    }

    //添加或修改solr中的数据
    @Override
    public void addOrUpdateSolr(Long itemid) throws Exception {
        // 1、根据商品id查询商品信息。
        SearchItem searchItem = tbItemMapper.findItemById(itemid);
        // 2、创建SolrInputDocument对象。
        SolrInputDocument document = new SolrInputDocument();
        // 3、使用SolrServer对象写入索引库。
        document.addField("id", searchItem.getId());
        document.addField("item_title", searchItem.getTitle());
        document.addField("item_sell_point", searchItem.getSell_point());
        document.addField("item_price", searchItem.getPrice());
        document.addField("item_image", searchItem.getImage());
        document.addField("item_category_name", searchItem.getCategory_name());
        document.addField("item_desc", searchItem.getItem_desc());
        // 5、向索引库中添加文档。
        solrServer.add(document);
        //提交
        solrServer.commit();
    }

    //删除solr的数据
    @Override
    public void deleteSolr(Long itemid) throws Exception {
        solrServer.deleteById(itemid + "");
        solrServer.commit();
    }
}
