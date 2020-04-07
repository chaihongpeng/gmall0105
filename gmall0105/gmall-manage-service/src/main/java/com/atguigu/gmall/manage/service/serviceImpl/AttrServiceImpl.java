package com.atguigu.gmall.manage.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.atguigu.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

@Service
public class AttrServiceImpl implements AttrService {
    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;
    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(PmsBaseAttrInfo pmsBaseAttrInfo) {
        return pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
//        id为空保存，id不为空修改
        if (StringUtils.isBlank(pmsBaseAttrInfo.getId())) {
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
            for (PmsBaseAttrValue pmsBaseAttrValue : pmsBaseAttrInfo.getAttrValueList()) {
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }
        } else {
            //属性修改
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id", pmsBaseAttrInfo.getId());
            pmsBaseAttrInfoMapper.updateByExample(pmsBaseAttrInfo,example);
            //属性值修改
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            //按照属性id删除属性
            PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
            pmsBaseAttrValueDel.setId(pmsBaseAttrInfo.getId());
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDel);
            //删除后将新的属性值插入
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                pmsBaseAttrValueMapper.insert(pmsBaseAttrValue);
            }
        }
        return "success";
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet) {
        if (valueIdSet.size() == 0) {
            return null;
        }
        String valueIdString = StringUtils.join(valueIdSet,",");
        return pmsBaseAttrInfoMapper.selectAttrValueListByValueId(valueIdString);
    }


}
