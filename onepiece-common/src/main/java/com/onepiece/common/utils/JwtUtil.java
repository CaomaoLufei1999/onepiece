package com.onepiece.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
    public static final long EXPIRE = 1000 * 60 * 60;

    /**
     * 密钥
     */
    public static final String JWT_SECRET = "ONEPIECE-SECURET";

    public static String getJwtToken(String id, String account) {

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")                             // typ: 表示令牌的类型
                .setHeaderParam("alg", "HS256")                           // alg: 表示签名使用的算法
                .setSubject("identity-authentication")                          // 主题: 身份认证
                .setIssuedAt(new Date())                                        // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))   // 设置过期时间
                .claim("id", id)
                .claim("account", account)
//                .addClaims(null)                                                // 将playload载荷添加到JWT令牌中
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
            return (String) claims.get("id");
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
            return (String) claims.get("account");
        } catch (Exception e) {
            logger.error("getMemberIdByJwtToken方法 token 解析失败：{}", e.getMessage());
            return null;
        }
    }

    public static String getUserAccountByJwtToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) return null;
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwtToken);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("account");
        } catch (Exception e) {
            logger.error("getMemberIdByJwtToken方法 token 解析失败：{}", e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        String jwtToken = JwtUtil.getJwtToken("0123456789", "onepiece");
        System.out.println(jwtToken);
        String account = JwtUtil.getUserAccountByJwtToken(jwtToken);
        System.out.println(account);
    }
}
