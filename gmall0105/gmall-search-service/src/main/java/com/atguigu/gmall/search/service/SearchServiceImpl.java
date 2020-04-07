package com.atguigu.gmall.search.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsSearchParam;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {
        //根据PmsSearchParam生成搜素语句
        String dslString = getSearchDsl(pmsSearchParam);

        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList();
        //包装sql语句，指定索引名，表名
        Search search = new Search.Builder(dslString).addIndex("gmall0105").addType("PmsSkuInfo").build();
        //执行search，并获取返回结果
        SearchResult execute = null;
        try {
            execute = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);
        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            //获取高亮集合
            Map<String, List<String>> highlight = hit.highlight;
            //获取query结果
            PmsSearchSkuInfo source = hit.source;
            //获取skuName的高亮字段
            if (highlight != null) {
                String skuName = highlight.get("skuName").get(0);
                //将原skuName替换为高亮字段
                source.setSkuName(skuName);
            }
            pmsSearchSkuInfos.add(source);

        }
        //返回获取的结果集
        return pmsSearchSkuInfos;
    }

    private String getSearchDsl(PmsSearchParam pmsSearchParam) {
        //获取查询条件的所有平台属性
        String[] valueIdList= pmsSearchParam.getValueId();
        //获取三级id
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        //获取搜索内容
        String keyword = pmsSearchParam.getKeyword();

        //jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //term
        //根据三级id过滤
        if (StringUtils.isNotBlank(catalog3Id)) {
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id", catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        //根据平台属性过滤
        if (valueIdList != null) {
            for (String valueId : valueIdList) {
                //term
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", valueId);
                //filter
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        //根据搜索词获取内容
        if (StringUtils.isNotBlank(keyword)) {
            //match
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", keyword);
            //must
            boolQueryBuilder.must(matchQueryBuilder);
        }
        //from
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(20);
        //query
        searchSourceBuilder.query(boolQueryBuilder);
        //highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //定义高亮字段
        highlightBuilder.field("skuName");
        //设置高亮字段的前后缀
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        //sort
        searchSourceBuilder.sort("id",SortOrder.DESC);
        //将于局转化为字符串
        return searchSourceBuilder.toString();
    }
}
