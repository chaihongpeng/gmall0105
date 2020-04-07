package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(PmsBaseAttrInfo pmsBaseAttrInfo);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet);
}
