package com.xianmao.utils;

import com.xianmao.date.DateTimeUtil;
import com.xianmao.exception.BizException;
import com.xianmao.rest.ResultCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.UUID;


/**
 * @ClassName JWTUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 20:02
 * @Version 1.0
 */
public class JWTUtil {

    private static Logger log = LoggerFactory.getLogger(JWTUtil.class);

    /**
     * JWT 加解密类型
     */
    private static final SignatureAlgorithm JWT_ALG = SignatureAlgorithm.HS256;
    /**
     * JWT 生成密钥使用的密码
     */
    private static final String JWT_RULE = "wjtree.xin";

    /**
     * JWT 添加至HTTP HEAD中的前缀
     */
    private static final String JWT_SEPARATOR = "Bearer ";

    /**
     * 使用JWT默认方式，生成加解密密钥
     *
     * @param alg 加解密类型
     * @return
     */
    public static SecretKey generateKey(SignatureAlgorithm alg) {
        return MacProvider.generateKey(alg);
    }

    /**
     * 构建JWT
     * <p>使用 UUID 作为 jti 唯一身份标识</p>
     * <p>JWT有效时间 3000 秒，即 10 分钟</p>
     *
     * @param sub jwt 面向的用户 即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志
     * @return JWT字符串
     */
    public static String buildJWT(String sub) {
        return buildJWT(sub, null, UUID.randomUUID().toString(), null, null, 3000);
    }

    /**
     * 构建JWT
     * <p>使用 UUID 作为 jti 唯一身份标识</p>
     * <p>JWT有效时间 3000 秒，即 10 分钟</p>
     *
     * @param sub      jwt 面向的用户 即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志
     * @param duration jwt 有效时间
     * @return JWT字符串
     */
    public static String buildJWT(String sub, Integer duration) {
        return buildJWT(sub, null, UUID.randomUUID().toString(), null, null, duration);
    }


    /**
     * 使用指定密钥生成规则，生成JWT加解密密钥
     *
     * @param alg  加解密类型
     * @param rule 密钥生成规则
     * @return SecretKey
     */
    public static SecretKey generateKey(SignatureAlgorithm alg, String rule) {
        // 将密钥生成键转换为字节数组
        byte[] bytes = Base64.decodeBase64(rule);
        // 根据指定的加密方式，生成密钥
        return new SecretKeySpec(bytes, alg.getJcaName());
    }

    /**
     * 构建JWT
     *
     * @param sub      jwt 面向的用户
     * @param aud      jwt 接收方
     * @param jti      jwt 唯一身份标识
     * @param iss      jwt 签发者
     * @param nbf      jwt 生效日期时间
     * @param duration jwt 有效时间，单位：秒
     * @return JWT字符串
     */
    public static String buildJWT(String sub, String aud, String jti, String iss, Date nbf, Integer duration) {
        return buildJWT(JWT_ALG, generateKey(JWT_ALG, JWT_RULE), sub, aud, jti, iss, nbf, duration);
    }

    /**
     * 构建JWT
     *
     * @param sub jwt 面向的用户
     * @param jti jwt 唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @return JWT字符串
     */
    public static String buildJWT(String sub, String jti, Integer duration) {
        return buildJWT(sub, null, jti, null, null, duration);
    }

    /**
     * 构建JWT
     *
     * @param alg      jwt 加密算法
     * @param key      jwt 加密密钥
     * @param sub      jwt 面向的用户
     * @param aud      jwt 接收方
     * @param jti      jwt 唯一身份标识
     * @param iss      jwt 签发者
     * @param nbf      jwt 生效日期时间
     * @param duration jwt 有效时间，单位：秒
     * @return JWT字符串
     */
    public static String buildJWT(SignatureAlgorithm alg, Key key, String sub, String aud, String jti, String iss, Date nbf, Integer duration) {
        // jwt的签发时间
        Date iat = new Date();
        // jwt的过期时间，这个过期时间必须要大于签发时间
        Date exp = null;
        if (duration != null)
            exp = (nbf == null ?
                    DateTimeUtil.plusSeconds(iat,duration) :
                    DateTimeUtil.plusSeconds(nbf,duration));

        // 获取JWT字符串
        String compact = Jwts.builder()
                .signWith(alg, key)
                .setSubject(sub)
                .setAudience(aud)
                .setId(jti)
                .setIssuer(iss)
                .setNotBefore(nbf)
                .setIssuedAt(iat)
                .setExpiration(exp != null ? exp : null)
                .compact();

        // 在JWT字符串前添加"Bearer "字符串，用于加入"Authorization"请求头
        return JWT_SEPARATOR + compact;
    }


    /**
     * 解析JWT
     *
     * @param key       jwt 加密密钥
     * @param claimsJws jwt 内容文本
     * @return {@link Jws}
     */
    public static Jws<Claims> parseJWT(Key key, String claimsJws) {
        try {
            // 移除 JWT 前的"Bearer "字符串
            claimsJws = StringUtils.substringAfter(claimsJws, JWT_SEPARATOR);
            // 解析 JWT 字符串
            return Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
        } catch (Exception e) {
            log.warn("解析jwt错误：{}", e.getMessage());
            throw new BizException(ResultCode.UNAUTHORIZED);
        }
    }

    /**
     * 解析JWT
     *
     * @param sub sub jwt 面向的用户 即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志
     * @return {@link Jws}
     */
    public static String parseJWTSub(String sub) {
        try {
            Jws<Claims> jws = JWTUtil.parseJWT(JWTUtil.generateKey(SignatureAlgorithm.HS256, "wjtree.xin"), sub);
            return String.valueOf(jws.getBody().get("sub"));
        } catch (Exception e) {
            log.warn("解析jwt错误：{}", e.getMessage());
            throw new BizException(ResultCode.UNAUTHORIZED);
        }
    }

    /**
     * 校验JWT
     *
     * @param claimsJws jwt 内容文本
     * @return ture or false
     */
    public static Boolean checkJWT(String claimsJws) {
        boolean flag = false;
        try {
            SecretKey key = generateKey(JWT_ALG, JWT_RULE);
            // 获取 JWT 的 payload 部分
            flag = (parseJWT(key, claimsJws).getBody() != null);
        } catch (Exception e) {
            log.warn("JWT验证出错，错误原因：{}", e.getMessage());
            throw new BizException(ResultCode.UNAUTHORIZED);
        }
        return flag;
    }

    /**
     * 校验JWT
     *
     * @param key       jwt 加密密钥
     * @param claimsJws jwt 内容文本
     * @param sub       jwt 面向的用户
     * @return ture or false
     */
    public static Boolean checkJWT(Key key, String claimsJws, String sub) {
        boolean flag = false;
        try {
            // 获取 JWT 的 payload 部分
            Claims claims = parseJWT(key, claimsJws).getBody();
            // 比对JWT中的 sub 字段
            flag = claims.getSubject().equals(sub);
        } catch (Exception e) {
            log.warn("JWT验证出错，错误原因：{}", e.getMessage());
            throw new BizException(ResultCode.UNAUTHORIZED);
        }
        return flag;
    }

    /**
     * 校验JWT
     *
     * @param claimsJws jwt 内容文本
     * @param sub       jwt 面向的用户
     * @return ture or false
     */
    public static Boolean checkJWT(String claimsJws, String sub) {
        try {
            return checkJWT(generateKey(JWT_ALG, JWT_RULE), claimsJws, sub);
        } catch (Exception e) {
            log.error("JWT验证出错，错误原因：{}", e.getMessage());
            throw new BizException(ResultCode.UNAUTHORIZED);
        }
    }
}
