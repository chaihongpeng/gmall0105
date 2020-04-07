package com.atguigu.gmall.manage.contoller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.service.SpuService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class SpuController {
    @Reference
    SpuService spuService;

    @RequestMapping("spuList")
    public List<PmsProductInfo> spuList(PmsProductInfo pmsProductInfo){
        return spuService.spuList(pmsProductInfo);
    }

    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile) {
        System.out.println("success");
        return "success";
    }

}
