package com.atguigu.gmall.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.beanutils.BeanUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GmallSearchServiceApplicationTests {
    @Reference
    SkuService skuService;
    @Autowired
    JestClient jestClient;

    @Test
    void contextLoads() throws IOException {
        //jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //term
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", "39");
        //terms
        TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("skuAttrValueList.valueId", "39", "43");
        //filter
        boolQueryBuilder.filter(termQueryBuilder);
        boolQueryBuilder.filter(termsQueryBuilder);
        //match
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", "小米");
        //must
        boolQueryBuilder.must(matchQueryBuilder);
        //query
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);
        searchSourceBuilder.query(boolQueryBuilder);
        String dslString = searchSourceBuilder.toString();
        System.out.println(dslString);


        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList();
        Search search = new Search.Builder(dslString).addIndex("gmall0105").addType("PmsSkuInfo").build();
        SearchResult execute = jestClient.execute(search);
        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);
        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;
            System.out.println(source);
            pmsSearchSkuInfos.add(source);

        }


    }

    @Test
    void put() throws IOException, InvocationTargetException, IllegalAccessException {
//        将数据库内容同步到es中
        List<PmsSkuInfo> pmsSkuInfos = skuService.getAllSku();
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        System.out.println(pmsSkuInfos);
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }
        System.out.println(pmsSearchSkuInfos);
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            Index put = new Index.Builder(pmsSearchSkuInfo).index("gmall0105").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()).build();
            jestClient.execute(put);
        }

    }

}


/**
 *
 *
 * GET gmall0105/PmsSkuInfo/_search
 * {
 *   "from": 0,
 *   "size": 20,
 *   "query":
 *   {
 *     "bool":
 *     {
 *       "filter":
 *       [
 *         {
 *           "terms":
 *           {
 *             "skuAttrValueList.valueId":
 *             [
 *               "39",
 *               "43"
 *             ]
 *           }
 *         },
 *         {
 *           "term":
 *           {
 *             "skuAttrValueList.valueId":"39"
 *           }
 *         }
 *       ]
 *       ,
 *       "must":
 *       [
 *         {
 *           "match": {
 *             "skuName": "小米"
 *           }
 *         },
 *         {
 *           "match":
 *           {
 *             "skuName": "陶瓷"
 *           }
 *         }
 *       ]
 *     }
 *   },
 *   "aggs":
 *   {
 *     "name":
 *     {
 *       "terms":
 *       {
 *         "field": "skuAttrValueList.valueId"
 *       }
 *     }
 *   }
 * }
 *
 *
 *
 *
 * */