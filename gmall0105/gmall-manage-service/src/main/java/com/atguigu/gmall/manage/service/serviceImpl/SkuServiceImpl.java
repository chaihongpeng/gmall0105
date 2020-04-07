package com.atguigu.gmall.manage.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuImage;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuImageMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuInfoMapper;
import com.atguigu.gmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    SkuService skuService;
    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        //设置商品sku信息查询条件
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        //设置商品sku图片列表信息查询条件
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        //根据id查询商品sku图片列表
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        //根据id查询商品sku信息列表
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        //将商品图片列表添加到到商品信息列表中
        if (pmsSkuInfo1 != null) {
            pmsSkuInfo1.setSkuImageList(pmsSkuImages);
        }
        return pmsSkuInfo1;
    }

    @Override
    @Cacheable(cacheNames = "PmsSkuInfo")
    public PmsSkuInfo getSkuByIdFromCache(String skuId) {
        //todo 处理缓存击穿和雪崩
        PmsSkuInfo pmsSkuInfo = null;
        //尝试加锁
        try {
            Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("PmsSkuInfo::lock", skuId, 20, TimeUnit.SECONDS);
            //加锁成功，从数据库中获取数值
            if (aBoolean) {
                pmsSkuInfo = getSkuById(skuId);
            }else {
                Thread.sleep(1000);
                pmsSkuInfo = skuService.getSkuByIdFromCache(skuId);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //判断是否是自己的锁，是的话最后解锁，不是就不执行
            if (redisTemplate.opsForValue().get("PmsSkuInfo::lock") == skuId) {
                redisTemplate.delete("PmsSkuInfo::lock");
            }
        }
        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }
        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {
        return pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
    }
}
