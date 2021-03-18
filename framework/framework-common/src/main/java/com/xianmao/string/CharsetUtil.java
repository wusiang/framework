package com.xianmao.string;

import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * @ClassName CharsetUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-30 16:50
 * @Version 1.0
 */
public class CharsetUtil {
    /**
     * 字符集ISO-8859-1
     */
    public static final java.nio.charset.Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
    /**
     * 字符集GBK
     */
    public static final java.nio.charset.Charset GBK = java.nio.charset.Charset.forName(StringPool.GBK);
    /**
     * 字符集utf-8
     */
    public static final java.nio.charset.Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * 转换为Charset对象
     *
     * @param charsetName 字符集，为空则返回默认字符集
     * @return Charsets
     * @throws UnsupportedCharsetException 编码不支持
     */
    public static Charset charset(String charsetName) throws UnsupportedCharsetException {
        return StringUtils.isEmpty(charsetName) ? java.nio.charset.Charset.defaultCharset() : java.nio.charset.Charset.forName(charsetName);
    }
}
