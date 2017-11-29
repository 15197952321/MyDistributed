package com.search.service.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml"})
public class SearchServiceTest {

    @Autowired
    private SolrServer solrServer;

    @Test
    public void test01() throws SolrServerException {
        //创建查询对象
        SolrQuery query = new SolrQuery();
        //设置条件
        query.set("df", "item_keywords");
        query.set("q", "手机");
        query.setStart(0);
        query.setRows(10);
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style='color:red'>");
        query.setHighlightSimplePost("</em>");

        //获取查询结果集对象
        QueryResponse response = solrServer.query(query);
        //获取正常结果集
        SolrDocumentList documentList = response.getResults();
        System.out.println("总条数：" + documentList.getNumFound());
        System.out.println("总页数：" + (documentList.getNumFound() + 10 - 1) / 10);
        //获取高亮结果集
        Map<String, Map<String, List<String>>> resultMap = response.getHighlighting();
        //遍历
        for (SolrDocument entries : documentList) {
            System.out.println(entries.get("id"));
            //判断标题中是否有高亮字体
            Object title = resultMap.get(entries.get("id")).get("item_title");
            if(title!=null){
                System.out.println(title);
            }else{
                System.out.println(entries.get("item_title"));
            }
            System.out.println(entries.get("item_price"));
            System.out.println(entries.get("item_image"));
            System.out.println(entries.get("item_sell_point"));
        }

    }

    @Test
    public void testAddOrUpdate() throws  Exception {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "test01");
        document.addField("item_title", "标题123123");
        solrServer.add(document);
        solrServer.commit();
    }

    @Test
    public void testDelete() throws Exception {
        solrServer.deleteById("test01");
        solrServer.commit();
    }

}
