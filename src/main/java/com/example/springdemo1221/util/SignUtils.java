package com.example.springdemo1221.util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;

public class SignUtils {

    private static final String secretKeyOfWxh = "e10adc3949ba59abbe56e057f20f883f";

    public static void main(String[] args) {
        //参数签名算法测试
        HashMap<String, String> signMap = new HashMap<String, String>();
        signMap.put("ticket","a2144337fdcd47daab3ed0623deb1817");
        signMap.put("personId","1");
        signMap.put("ts","1609827917");
        System.out.println("得到签名sign1:"+getSign(signMap,secretKeyOfWxh));
    }


    /**
     * @param request
     * @return
     */
    public static Boolean checkSign(HttpServletRequest request,String sign){
        Boolean flag= false;
        //检查sigin是否过期
        Enumeration<?> pNames =  request.getParameterNames();
        Map<String, String> params = new HashMap<String, String>();
        while (pNames.hasMoreElements()) {
            String pName = (String) pNames.nextElement();
            if("sign".equals(pName)) continue;
            String pValue = (String)request.getParameter(pName);
            params.put(pName, pValue);
        }
        System.out.println("现在的sign-->>" + sign);
        System.out.println("验证的sign-->>" + getSign(params,secretKeyOfWxh));
        if(sign.equals(getSign(params, secretKeyOfWxh))){
            flag = true;
        }
        return flag;
    }


    public static String utf8Encoding(String value, String sourceCharsetName) {
        try {
            return new String(value.getBytes(sourceCharsetName), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }



    private static byte[] getMD5Digest(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse);
        }
        return bytes;
    }


    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }


    /**
     * 得到签名
     * @param params 参数集合不含secretkey
     * @param secret 验证接口的secretkey
     * @return
     */
    public static String getSign(Map<String, String> params,String secret)
    {
        String sign="";
        StringBuilder sb = new StringBuilder();
        //step1：先对请求参数排序
        Set<String> keyset=params.keySet();
        TreeSet<String> sortSet=new TreeSet<String>();
        sortSet.addAll(keyset);
        Iterator<String> it=sortSet.iterator();
        //step2：把参数的key value链接起来 secretkey放在最后面，得到要加密的字符串
        while(it.hasNext())
        {
            String key=it.next();
            String value=params.get(key);
            sb.append(key).append(value);
        }
        sb.append(secret);
        byte[] md5Digest;
        try {
            //得到Md5加密得到sign
            md5Digest = getMD5Digest(sb.toString());
            sign = byte2hex(md5Digest);
        } catch (IOException e) {
            System.out.println("生成签名错误");
        }
        return sign;
    }

    public static long getTimestamp(){
        long timestampLong = System.currentTimeMillis();

        long timestampsStr = timestampLong / 1000;

        return timestampsStr;
    }
}
