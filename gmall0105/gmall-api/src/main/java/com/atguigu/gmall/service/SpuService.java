package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;

import java.util.List;

public interface SpuService {
    List<PmsProductInfo> spuList(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
