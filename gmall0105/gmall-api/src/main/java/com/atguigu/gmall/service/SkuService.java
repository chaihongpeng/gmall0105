package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsSkuInfo;

import java.util.List;

public interface SkuService {

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId);

    PmsSkuInfo getSkuByIdFromCache(String skuId);

    List<PmsSkuInfo> getAllSku();
}
