package com.example.springdemo1221.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springdemo1221.entity.PersonDao;
import com.example.springdemo1221.entity.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("tokenSignService")
public class TokenSignService {

    @Resource
    private PersonDao personDao;

    @Autowired
    private RedisTemplate redisTemplate;

    public List<PersonEntity> getPersonList(int personId){
        List<PersonEntity> list = personDao.findById(personId);
        return list;
    }
    /** 
     * 
     * Description:验证登录，ticket成功后放置缓存中,
     * @param
     * @author huangweicheng
     * @date 2020/12/31   
    */ 
    public JSONObject login(String username,String password){
        JSONObject result = new JSONObject();
        PersonEntity personEntity = personDao.findByLoginName(username);
        if (personEntity == null || (personEntity != null && !personEntity.getPassword().equals(password))){
            result.put("success",false);
            result.put("ticket","");
            result.put("code","999");
            result.put("message","用户名和密码不匹配");
            return result;
        }
        if (personEntity.getLoginName().equals(username) && personEntity.getPassword().equals(password)){
            String ticket = UUID.randomUUID().toString();
            ticket = ticket.replace("-","");
            redisTemplate.opsForValue().set(ticket,personEntity.getLoginName(),10L, TimeUnit.MINUTES);
            result.put("success",true);
            result.put("ticket",ticket);
            result.put("code",200);
            result.put("message","登录成功");
            return result;
        }
        result.put("success",false);
        result.put("ticket","");
        result.put("code","1000");
        result.put("message","未知异常，请重试");
        return result;
    }
}
