package com.atguigu.gmall.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Component
public class ItemController {

    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;

    @RequestMapping("{skuId}.html")
    public String index(@PathVariable String skuId, ModelMap modelMap) {
        //根据sku的id，查询出对应商品的sku信息，及所需的图片的来源信息
        PmsSkuInfo pmsSkuInfo = skuService.getSkuByIdFromCache(skuId);
        modelMap.put("skuInfo", pmsSkuInfo);
        //通过spu的id，查找spu的销售属性列表
        if (pmsSkuInfo !=null) {
            List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),pmsSkuInfo.getId());
            modelMap.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrs);
            //根据sku查找spu中其他sku的
            List<PmsSkuInfo> pmsSkuInfos =  skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
            //将每种属性对应的sku存入hashMap中
            Map skuSaleAttrHashMap = new HashMap();
            for (PmsSkuInfo pmsSkuInfo1 : pmsSkuInfos) {
                StringBuilder k = new StringBuilder();
                String v = pmsSkuInfo1.getId();
                List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo1.getSkuSaleAttrValueList();
                for (PmsSkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                    k.append(skuSaleAttrValue.getSaleAttrValueId()).append("|");
                }
                skuSaleAttrHashMap.put(k.toString(),v);
            }
            String skuSaleAttrHashMapJSONStr = JSON.toJSONString(skuSaleAttrHashMap);
            modelMap.put("skuSaleAttrHashMapJSONStr",skuSaleAttrHashMapJSONStr);
        }

        return "item";
    }



}
