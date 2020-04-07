package com.atguigu.gmall.manage.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsProductSaleAttrValue;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.manage.mapper.PmsProductInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
    @Override
    public List<PmsProductInfo> spuList(PmsProductInfo pmsProductInfo) {
        return pmsProductInfoMapper.select(pmsProductInfo);
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId) {
        //根据商品spu的id，查询 销售属性和销售属性值
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.selectSpuSaleAttrListBySpuId(productId,skuId);
        return pmsProductSaleAttrs;
    }
}
