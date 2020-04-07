package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo> {
    List<PmsBaseAttrInfo> selectAttrValueListByValueId(String valueIdString);
}
