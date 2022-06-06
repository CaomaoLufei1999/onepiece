package com.onepiece.common.utils;

import com.alibaba.fastjson.JSON;
import com.onepiece.common.pojo.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @描述 JWT工具类: JWT = 头部Header + 载荷playload + 签名signature
 * @作者 天天发呆的程序员
 * @创建时间 2022-06-02
 */
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * token过期时间：1小时
     */
//    public static final long EXPIRE = 1000 * 60 * 60;
    public static final long EXPIRE = 1000 * 60 * 3;

    /**
     * 密钥
     */
    public static final String JWT_SECRET = "ONEPIECE-SECURET";

    public static String getJwtToken(String userId, String userInfo) {

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")                             // typ: 表示令牌的类型
                .setHeaderParam("alg", "HS256")                           // alg: 表示签名使用的算法
                .setSubject("identity-authentication")                          // 主题: 身份认证
                .setIssuedAt(new Date())                                        // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))   // 设置过期时间
                .claim("userId", userId)
                .claim("userInfo", userInfo)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)                 // 签名算法以及密匙
                .compact();
    }

    public static String getJwtToken(UserInfo userInfo) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")                             // typ: 表示令牌的类型
                .setHeaderParam("alg", "HS256")                           // alg: 表示签名使用的算法
                .setSubject("identity-authentication")                          // 主题: 身份认证
                .setIssuedAt(new Date())                                        // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))   // 设置过期时间
                .claim("userInfo", userInfo)                                 // 将playload载荷添加到JWT令牌中
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)                 // 签名算法以及密匙
                .compact();
    }

    /**
     * 根据token，判断token是否存在有效时间内
     *
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            logger.error("checkToken方法 token 解析失败：{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 根据request判断token是否存在与有效（也就是把token取出来罢了）
     *
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("UserToken");
            if (StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            logger.error("checkToken方法 token 解析失败：{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 根据token获取用户id
     *
     * @param request
     * @return
     */
    public static String getUserIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("UserToken");
        if (StringUtils.isEmpty(jwtToken)) return null;
        try {
            // 这里解析可能会抛异常，所以try catch来捕捉
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("userId");
        } catch (Exception e) {
            logger.error("getUserIdByJwtToken方法 token 解析失败：{}", e.getMessage());
            return null;
        }
    }


    /**
     * 根据token获取用户的account
     *
     * @param request
     * @return
     */
    public static String getUserAccountByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("UserToken");
        if (StringUtils.isEmpty(jwtToken)) return null;
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("userInfo");
        } catch (Exception e) {
            logger.error("getMemberIdByJwtToken方法 token 解析失败：{}", e.getMessage());
            return null;
        }
    }

    public static UserInfo getUserInfoByJwtToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) return null;
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();
            String userInfoStr = JSON.toJSONString(claims.get("userInfo"));
            return JSON.parseObject(userInfoStr, UserInfo.class);
        } catch (Exception e) {
            logger.error("getUserInfoByJwtToken方法 token 解析失败：{}", e.getMessage());
            return null;
        }
    }

    /**
     * java对象转Map
     *
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static Map javabeanToMap(Object object) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(object));
        }
        return map;
    }

    /**
     * map转java对象
     *
     * @param map
     * @param beanClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T mapToJavabean(Map map, Class<T> beanClass) throws Exception {
        T object = beanClass.newInstance();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                field.set(object, map.get(field.getName()));
            }
        }
        return object;
    }

    public static void main(String[] args) {
//        String jwtToken = JwtUtil.getJwtToken("0123456789", "onepiece");
//        System.out.println(jwtToken);
//        UserInfo userInfo = JwtUtil.getUserInfoByJwtToken(jwtToken);
//        UserInfo userInfo = JwtUtil.getUserInfoByJwtToken("");
        UserInfo userInfo = JwtUtil.getUserInfoByJwtToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpZGVudGl0eS1hdXRoZW50aWNhdGlvbiIsImlhdCI6MTY1NDI0OTQ5NCwiZXhwIjoxNjU0MjQ5Njc0LCJ1c2VySW5mbyI6eyJ1c2VySWQiOjYsIm9wZW5pZCI6bnVsbCwidW5pb25pZCI6bnVsbCwibmlja25hbWUiOm51bGwsInNleCI6MSwiY291bnRyeSI6IuS4reWbvSIsInByb3ZpbmNlIjpudWxsLCJjaXR5IjpudWxsLCJhZGRyZXNzIjpudWxsLCJhY2NvdW50QmluZGluZ1R5cGUiOiJRUSIsImhlYWRJbWciOm51bGwsInBob25lIjpudWxsLCJlbWFpbCI6bnVsbCwidXNlcm5hbWUiOm51bGwsInBhc3N3b3JkIjpudWxsLCJzZWN1cml0eVF1ZXN0aW9ucyI6bnVsbCwiaW50ZXJlc3RUYWdzIjpudWxsLCJmcmllbmRMaW5rcyI6bnVsbCwiZGVzY3JpcHRpb24iOm51bGwsInByaXZpbGVnZSI6MCwiaWRlbnRpdHkiOm51bGwsImxldmVsIjowLCJwb2ludHMiOjAsImNyZWF0ZVRpbWUiOjE2NTQyNDk0OTMyMjksImVuYWJsZWQiOm51bGx9fQ.eRzYMhVwBNi1Iq5mrbM-JgmnaCdHQt8AsRMXNFbzBSI");
        System.out.println(userInfo);
    }
}
