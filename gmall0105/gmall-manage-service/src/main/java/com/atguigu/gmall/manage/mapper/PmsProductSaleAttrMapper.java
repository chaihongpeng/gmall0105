package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsProductSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    List<PmsProductSaleAttr> selectSpuSaleAttrListBySpuId(String productId,String skuId);
}
