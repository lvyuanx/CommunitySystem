package com.lvyx.author.shiro.core.impl;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.lvyx.author.shiro.cache.SimpleMapCache;
import com.lvyx.author.shiro.cache.service.SimpleMapCacheService;
import com.lvyx.author.shiro.config.JWTProperties;
import com.lvyx.commons.enums.ShiroCacheEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.shiro.cache.Cache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("jwtTokenManager")
public class JwtTokenManager {

    @Resource
    private JWTProperties jwtProperties;

    @Resource
    private SimpleMapCacheService simpleMapCacheService;

    /**
     * 签发令牌
     *      jwt字符串包括三个部分
     *        1. header
     *            -当前字符串的类型，一般都是“JWT”
     *            -哪种算法加密，“HS256”或者其他的加密算法
     *            所以一般都是固定的，没有什么变化
     *        2. payload
     *            一般有四个最常见的标准字段（下面有）
     *            iat：签发时间，也就是这个jwt什么时候生成的
     *            jti：JWT的唯一标识
     *            iss：签发人，一般都是username或者userId
     *            exp：过期时间
     * @param iss       签发人
     * @param ttlMillis  有效时间
     * @param sessionId sessionId
     * @param claims    jwt中存储的非隐私信息
     * @return java.lang.String
     * @author lvyx
     * @since 2022/1/14 9:36
     **/
    public String issuedToken(String iss, long ttlMillis, String sessionId, Map<String, Object> claims){
        if (ObjectUtil.isEmpty(claims)){
            claims = new HashMap<>();
        }
        //获取当前时间
        long nowMillis = System.currentTimeMillis();
        //获取加密签名
        String base64EncodedSecretKey = Base64.encode(jwtProperties.getHexEncodedSecretKey().getBytes());
        //构建令牌
        JwtBuilder builder = Jwts.builder()
                //构建非隐私信息
                .setClaims(claims)
                //构建唯一标识，此时使用shiro生成的唯一id
                .setId(sessionId)
                //构建签发时间
                .setIssuedAt(new Date(nowMillis))
                //签发者
                .setSubject(iss)
                //指定算法和秘钥
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey);
        if (ttlMillis>0){
            long expMillis = nowMillis+ttlMillis;
            Date expData = new Date(expMillis);
            //指定过期时间
            builder.setExpiration(expData);
        }
        String jwtStr = builder.compact();
        // 将jwtToken字符串保存到缓存中
        Map<Object, Object> map = new HashMap<>();
        String key = ShiroCacheEnum.JWT_TOKEN.getValue() + sessionId;
        map.put(key, jwtStr);
        simpleMapCacheService.createCache(key, new SimpleMapCache(key, map));
        // 返回缓存的id
        return sessionId;
    }


    /**
     * 解析令牌
     * @param tokenId 令牌字符串
     * @return Claims
     */
    public Claims decodeToken(String tokenId){
        // 缓存中拿实际的token
        String key = ShiroCacheEnum.JWT_TOKEN.getValue() + tokenId;
        Cache<Object, Object> cache = simpleMapCacheService.getCache(key);
        String jwtToken = null;
        if (! ObjectUtil.isNull(cache)) {
            jwtToken = (String) cache.get(key);
        }
        //获取加密签名
        String base64EncodedSecretKey = Base64.encode(jwtProperties.getHexEncodedSecretKey().getBytes());
        //带着密码去解析字符串
        return Jwts.parser()
                .setSigningKey(base64EncodedSecretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    /**
     * 校验令牌:1、头部信息和荷载信息是否被篡改 2、校验令牌是否过期
     * @param  tokenId 令牌字符串id
     * @return boolean
     */
    public boolean isVerifyToken(String tokenId){
        // 缓存中拿实际的token
        String key = ShiroCacheEnum.JWT_TOKEN.getValue() + tokenId;
        Cache<Object, Object> cache = simpleMapCacheService.getCache(key);
        String jwtToken = null;
        if (! ObjectUtil.isNull(cache)) {
            jwtToken = (String) cache.get(key);
        }
        //带着签名构建校验对象
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getHexEncodedSecretKey().getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        //校验:如果校验1、头部信息和荷载信息是否被篡改 2、校验令牌是否过期 不通过则会抛出异常
        try {
            jwtVerifier.verify(jwtToken);
        } catch (Exception e){
            return false;
        }
        return true;
    }

}