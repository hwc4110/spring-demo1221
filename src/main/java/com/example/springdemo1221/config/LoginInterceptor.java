package com.example.springdemo1221.config;
import com.alibaba.fastjson.JSONObject;
import com.example.springdemo1221.util.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws IOException {
        JSONObject jsonObject = new JSONObject();
        String ticket = request.getParameter("ticket");
        String sign = request.getParameter("sign");
        String ts = request.getParameter("ts");
        if (StringUtils.isEmpty(ticket) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(ts)){
            jsonObject.put("success",false);
            jsonObject.put("message","args is isEmpty");
            jsonObject.put("code","1001");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(jsonObject.toJSONString());
            return false;
        }
        //如果redis存在ticket就认为是合法的请求
        if (redisTemplate.hasKey(ticket)){
            System.out.println(redisTemplate.opsForValue().getOperations().getExpire(ticket));
            String values = (String) redisTemplate.opsForValue().get(ticket);
            //判断ticket是否即将过期，进行续命操作
            if (redisTemplate.opsForValue().getOperations().getExpire(ticket) != -2 && redisTemplate.opsForValue().getOperations().getExpire(ticket) < 20){
                redisTemplate.opsForValue().set(ticket,values,10L, TimeUnit.MINUTES);
            }
            System.out.println(SignUtils.getTimestamp());
            //判断是否重复访问，存在重放攻击的时间窗口期
            if (SignUtils.getTimestamp() - Long.valueOf(ts) > 600){
                jsonObject.put("success",false);
                jsonObject.put("message","Overtime to connect to server");
                jsonObject.put("code","1002");
                PrintWriter printWriter = response.getWriter();
                printWriter.write(jsonObject.toJSONString());
                return false;
            }
            //验证签名
            if (!SignUtils.checkSign(request,sign)){
                jsonObject.put("success",false);
                jsonObject.put("message","sign is invalid");
                jsonObject.put("code","1003");
                PrintWriter printWriter = response.getWriter();
                printWriter.write(jsonObject.toJSONString());
                return false;
            }
            return true;
        }else {
            jsonObject.put("success",false);
            jsonObject.put("message","ticket is invalid,Relogin.");
            jsonObject.put("code","1004");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(jsonObject.toJSONString());
        }
        return false;
    }
}
