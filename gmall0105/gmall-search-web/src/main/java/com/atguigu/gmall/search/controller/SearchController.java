package com.atguigu.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;


@Controller
public class SearchController {
    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {
        //根据请求条件pmsSearchParam，查询sku商品信息集合
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        //平台属性值id
        Set<String> valueIdSet = new HashSet<String>();

        //取出所有sku商品信息集合中所拥有的的平台属性id,并通过valueIdSet集合去重
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                valueIdSet.add(pmsSkuAttrValue.getValueId());
            }
        }

        //根据valueId的set集合查出平台属性
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.getAttrValueListByValueId(valueIdSet);
        modelMap.put("attrList", pmsBaseAttrInfos);

        //已经被选中的平台属性值集合id
        String[] selectedValueId = pmsSearchParam.getValueId();
        if (selectedValueId != null) {
            //面包屑列表
            List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
            //遍历平台属性值id集合，制作面包屑并剔除已选中的平台属性筛选列表
            for (String delValueId : selectedValueId) {
                //制作面包屑
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParam(pmsSearchParam, delValueId));
                pmsSearchCrumbs.add(pmsSearchCrumb);

                //迭代平台属性值列表，剔除已被选中的平台属性
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String valueId = pmsBaseAttrValue.getId();
                        if (delValueId.equals(valueId)) {
                            //根据已被选中的平台属性值id，获取面包屑的平台属性值名称
                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            iterator.remove();
                        }
                    }
                }
            }
            modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
        }

        modelMap.put("skuLsInfoList", pmsSearchSkuInfos);

        //将请求条件转化为url拼接字符串
        String url = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam", url);


        return "list";
    }

    //根据查询条件生成url,或面包屑请求生成
    private String getUrlParam(PmsSearchParam pmsSearchParam, String... delValueId) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] valueIdList = pmsSearchParam.getValueId();

        StringBuilder urlParam = new StringBuilder();
        //拼接keyword
        if (StringUtils.isNotBlank(keyword)) {
            urlParam.append("keyword=").append(keyword);
        }
        //拼接catalog3Id
        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) urlParam.append("&");
            urlParam.append("catalog3Id=").append(catalog3Id);
        }
        //拼接平台属性值
        if (valueIdList != null) {
            for (String valueId : valueIdList) {
                //如果delValueId为空，则直接生成请求url，如果delValueId不为空，则剔除已被选中的字符再生成url
                if (delValueId.length == 0 || !valueId.equals(delValueId[0])) {
                    if (StringUtils.isNotBlank(urlParam)) urlParam.append("&");
                    urlParam.append("valueId=").append(valueId);
                }
            }
        }
        return urlParam.toString();
    }
}
