package com.atguigu.gmall.user.service.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.user.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> umsMembers = userMapper.selectAll();
        return umsMembers;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
        return umsMemberReceiveAddresses;
    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        return userMapper.selectOne(umsMember);
    }

    @Override
    public UmsMember loginFromCache(UmsMember umsMember) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String password = (String) valueOperations.get("UmsMember:" + umsMember.getUsername() + ":password");
        if (StringUtils.isBlank(password)) {
            //redis中未取到值则通过数据库取值
            UmsMember umsMemberFromDB = userMapper.selectOne(umsMember);
            if (umsMemberFromDB != null) {
                valueOperations.set("UmsMember:" +umsMemberFromDB.getUsername() + ":" + umsMemberFromDB.getPassword() + ":info" ,umsMemberFromDB,47,TimeUnit.HOURS);
                valueOperations.set("UmsMember:" + umsMemberFromDB.getUsername() + ":password", umsMemberFromDB.getPassword(), 48,TimeUnit.HOURS);
            }
            return umsMemberFromDB;
        }
        if (password.equals(umsMember.getPassword())) {
            //redis中的密码等于用户登录密码
            UmsMember umsMemberinfo = (UmsMember) valueOperations.get("UmsMember:" + umsMember.getUsername() + ":" + umsMember.getPassword() + ":info");
            return umsMemberinfo;
        }
        return null;
    }

    @Override
    public void addUserToken(String token, String memberId) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("user:" +memberId+ ":token",token,2, TimeUnit.HOURS);
    }


}
