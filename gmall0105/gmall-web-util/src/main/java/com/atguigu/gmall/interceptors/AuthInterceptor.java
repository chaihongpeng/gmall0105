package com.atguigu.gmall.interceptors;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.util.CookieUtil;
import com.atguigu.gmall.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 这是一个登录拦截器，负责拦截所有标注了@LoginRequired注解的方法
 * 校验当前用户是否已经登录，将未登录的用户的请求跳转到登录界面
 * 同时校验已登录的用户的签名是否正确
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof ResourceHttpRequestHandler) return true;
        LoginRequired methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(LoginRequired.class);
        //判断未标注@LoginRequired注解的请求，无需进行登录校验
        if (methodAnnotation == null) return true;
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
        String newToken = request.getParameter("token");
        //尝试从地址栏和cookie中获取token，并优先使用地址栏的新token，如果都为空则返回空串
        String token = StringUtils.isNotBlank(newToken) ? newToken : StringUtils.isNotBlank(oldToken) ? oldToken : "";

        String success = "fail";
        Map<String,String> successMap = new HashMap<>();
        //调用认证中心进行token验证,如果token为空则直接以验证失败处理
        if (StringUtils.isNotBlank(token)) {
            //todo gamll-passport-web 的ip和端口号
            String successJson = HttpclientUtil.doGet("http://localhost:8085/verify?token=" + token+"&currentIp=" + request.getRemoteAddr());
            successMap = JSON.parseObject(successJson, Map.class);
            success = successMap.get("status");
        }
        //获取注解参数，判断是否必须登录成功
        if (methodAnnotation.loginSuccess()) {
            //如果为必须登录请求,验证token是否有效
            if (!success.equals("success")) {
                //验证未通过，重定向到登录界面
                //todo gmall-passport-web 的ip和端口号
                response.sendRedirect("http://localhost:8085/index?ReturnUrl=" + request.getRequestURL());
                return false;
            }

            //将token中携带的用户信息写入
            request.setAttribute("memberId", successMap.get("memberId"));
            request.setAttribute("nickname", successMap.get("nickName"));
            //验证通过，覆盖cookie中的token
            if (StringUtils.isNotBlank(token)) {
                CookieUtil.setCookie(request, response, "oldToken", token,60*60*2,true);
            }

        } else {
            //没有登录也会放行
            if (success.equals("success")) {
                //如果登录成功则从token中取出memberId
                request.setAttribute("memberId", successMap.get("memberId"));
                request.setAttribute("nickname", successMap.get("nickName"));
                //验证通过，覆盖cookie中的token
                if (StringUtils.isNotBlank(token)) {
                    CookieUtil.setCookie(request, response, "oldToken", token,60*60*2,true);
                }
            }
        }

        return true;
    }
}
