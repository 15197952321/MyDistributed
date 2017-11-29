package com.search.service;

import com.common.result.SCResult;
import com.common.result.SearchItem;
import org.apache.solr.client.solrj.SolrServerException;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/21.
 */
public interface SearchItemService {

    //导入数据到solr中
    SCResult findAllItemByInputSolr();

    //从solr查询数据
    Map<String, Object> selectSolr(String keyWord, Integer page, Integer rows) throws SolrServerException;

    //添加或修改solr中的数据
    void addOrUpdateSolr(Long itemId) throws Exception;

    //删除solr的数据
    void deleteSolr(Long itemId) throws Exception;


}
