package com.atguigu.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.util.JwtUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {
    @Reference
    UserService userService;

    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap modelMap) {
        modelMap.put("ReturnUrl", ReturnUrl);
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember, HttpServletRequest httpServletRequest) {
        //调用用户服务，验证用户名和密码
        UmsMember userMember = userService.loginFromCache(umsMember);
        if (userMember != null) {
            //制作token
            String ip = httpServletRequest.getRemoteAddr();
            HashMap<String, Object> userMap = new HashMap<>();
            String memberId = userMember.getId();
            userMap.put("memberId",memberId);
            userMap.put("nickName", userMember.getNickname());
            //转化令牌
            String token = JwtUtil.encode("2020gmall0105", userMap, ip);
            //将令牌保存在redis中一份
            userService.addUserToken(token,memberId);
            //登录成功
            return token;
        } else {
            //登录失败
            return "fail";
        }
    }

    @RequestMapping("verify")
    @ResponseBody
    public Map<String, String> verify(String token, String currentIp) {
        Map<String, String> map = new HashMap<>();
        Map<String, Object> decode = JwtUtil.decode(token, "2020gmall0105", currentIp);
        if (decode != null) {
            map.put("status", "success");
            map.put("memberId", (String) decode.get("memberId"));
            map.put("nickName", (String) decode.get("nickName"));
        }else {
            map.put("status", "fail");
        }
        return map;
    }

}
