package com.xianmao.support;

        import java.io.OutputStream;

/**
 * @ClassName IMultiOutputStream
 * @Description: TODO
 * @Author guyi
 * @Data 2020-01-16 17:26
 * @Version 1.0
 */
public interface IMultiOutputStream {

    /**
     * Builds the output stream.
     *
     * @param params the params
     * @return the output stream
     */
    OutputStream buildOutputStream(Integer... params);
}
