package com.atguigu.gmall.manage.contoller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.bean.PmsBaseCatalog2;
import com.atguigu.gmall.bean.PmsBaseCatalog3;
import com.atguigu.gmall.service.CatalogService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CatalogController {

    @Reference
    CatalogService catalogService;

    @RequestMapping("getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1() {
        return catalogService.getCatalog1();
    }

    @RequestMapping("getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(PmsBaseCatalog2 pmsBaseCatalog2){
        return catalogService.getCatalog2(pmsBaseCatalog2);
    }
    @RequestMapping("getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(PmsBaseCatalog3 pmsBaseCatalog3){
        return catalogService.getCatalog3(pmsBaseCatalog3);
    }

}
