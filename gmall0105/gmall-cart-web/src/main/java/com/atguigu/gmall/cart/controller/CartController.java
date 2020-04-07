package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;

@Controller
public class CartController {
    @Reference
    SkuService skuService;

    //添加购物车服务
    @LoginRequired
    @RequestMapping("addToCart")
    public String addToCart(String skuId, Integer quantity,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            HttpSession session) {//商品的skuId,和商品数量
        //调用商品服务
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId);
        //将商品信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getProductId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(new BigDecimal(quantity));
        //todo 判断用户是否登陆
        String memerId = "";
        //根据用户登录状态决定购物车信息处理分支。
        if (StringUtils.isBlank(memerId)) {
            //用户未登录,使用cookie缓存
            //todo cookie的跨域问题

        } else {
            //用户已登录，使用db和cache
        }
        return "redirect:/success.html";
    }

}
