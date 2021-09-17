package com.xianmao.common.utils;

import com.xianmao.common.exception.UtilException;
import org.springframework.util.Assert;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @ClassName IOUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 10:19
 * @Version 1.0
 */
public class IoUtil {

    /**
     * 默认缓存大小 8192
     */
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;
    /**
     * 默认中等缓存大小 16384
     */
    public static final int DEFAULT_MIDDLE_BUFFER_SIZE = 2 << 13;
    /**
     * 默认大缓存大小 32768
     */
    public static final int DEFAULT_LARGE_BUFFER_SIZE = 2 << 14;

    /**
     * 数据流末尾
     */
    public static final int EOF = -1;

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int ch;
        while ((ch = is.read(buffer)) != -1) {
            bytestream.write(buffer, 0, ch);
        }
        byte data[] = bytestream.toByteArray();
        bytestream.close();
        return data;
    }

    public static void copyToFile(InputStream is, File dest) {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int n = 0;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dest);
            while (EOF != (n = is.read(buffer))) {
                fos.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(fos);
            closeQuietly(is);
        }
    }

    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(baos);
            closeQuietly(oos);
        }
        return null;
    }

    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            closeQuietly(bais);
            closeQuietly(ois);
        }
        return null;
    }

    // -------------------------------------------------------------------------------------- Copy start
    /**
     * 将Reader中的内容复制到Writer中 使用默认缓存大小，拷贝后不关闭Reader
     *
     * @param reader Reader
     * @param writer Writer
     * @return 拷贝的字节数
     * @throws UtilException IO异常
     */
    public static long copy(Reader reader, Writer writer) throws UtilException {
        return copy(reader, writer, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
     *
     * @param reader         Reader
     * @param writer         Writer
     * @param bufferSize     缓存大小
     * @return 传输的byte数
     * @throws UtilException IO异常
     */
    public static long copy(Reader reader, Writer writer, int bufferSize) throws UtilException {
        char[] buffer = new char[bufferSize];
        long size = 0;
        int readSize;
        try {
            while ((readSize = reader.read(buffer, 0, bufferSize)) != EOF) {
                writer.write(buffer, 0, readSize);
                size += readSize;
                writer.flush();
            }
        } catch (Exception e) {
            throw new UtilException(e);
        }
        return size;
    }

    /**
     * 拷贝流，使用默认Buffer大小，拷贝后不关闭流
     *
     * @param in  输入流
     * @param out 输出流
     * @return 传输的byte数
     * @throws UtilException IO异常
     */
    public static long copy(InputStream in, OutputStream out) throws UtilException {
        return copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 拷贝流，拷贝后不关闭流
     *
     * @param in             输入流
     * @param out            输出流
     * @param bufferSize     缓存大小
     * @return 传输的byte数
     * @throws UtilException IO异常
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize) throws UtilException {
        Assert.notNull(in, "InputStream is null !");
        Assert.notNull(out, "OutputStream is null !");
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        byte[] buffer = new byte[bufferSize];
        long size = 0;
        try {
            for (int readSize; (readSize = in.read(buffer)) != EOF; ) {
                out.write(buffer, 0, readSize);
                size += readSize;
                out.flush();
            }
        } catch (IOException e) {
            throw new UtilException(e);
        }
        return size;
    }

    /**
     * 拷贝流 thanks to: https://github.com/venusdrogon/feilong-io/blob/master/src/main/java/com/feilong/io/IOWriteUtil.java<br>
     * 本方法不会关闭流
     *
     * @param in             输入流
     * @param out            输出流
     * @param bufferSize     缓存大小
     * @return 传输的byte数
     * @throws UtilException IO异常
     */
    public static long copyByNIO(InputStream in, OutputStream out, int bufferSize) throws UtilException {
        return copy(Channels.newChannel(in), Channels.newChannel(out), bufferSize);
    }

    /**
     * 拷贝文件流，使用NIO
     *
     * @param in  输入
     * @param out 输出
     * @return 拷贝的字节数
     * @throws UtilException IO异常
     */
    public static long copy(FileInputStream in, FileOutputStream out) throws UtilException {
        Assert.notNull(in, "FileInputStream is null!");
        Assert.notNull(out, "FileOutputStream is null!");

        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = in.getChannel();
            outChannel = out.getChannel();
            return inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw new UtilException(e);
        } finally {
            close(outChannel);
            close(inChannel);
        }
    }

    /**
     * 拷贝流，使用NIO，不会关闭流
     *
     * @param in  {@link ReadableByteChannel}
     * @param out {@link WritableByteChannel}
     * @return 拷贝的字节数
     * @throws UtilException IO异常
     * @since 4.5.0
     */
    public static long copy(ReadableByteChannel in, WritableByteChannel out) throws UtilException {
        return copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 拷贝流，使用NIO，不会关闭流
     *
     * @param in             {@link ReadableByteChannel}
     * @param out            {@link WritableByteChannel}
     * @param bufferSize     缓冲大小，如果小于等于0，使用默认
     * @return 拷贝的字节数
     * @throws UtilException IO异常
     */
    public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize) throws UtilException {
        Assert.notNull(in, "InputStream is null !");
        Assert.notNull(out, "OutputStream is null !");

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize);
        long size = 0;
        try {
            while (in.read(byteBuffer) != EOF) {
                byteBuffer.flip();// 写转读
                size += out.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            throw new UtilException(e);
        }

        return size;
    }

    /**
     * 从流中读取bytes，读取完毕后关闭流
     *
     * @param in {@link InputStream}
     * @return bytes
     * @throws UtilException IO异常
     */
    public static byte[] readBytes(InputStream in) throws UtilException {
        return readBytes(in, true);
    }

    /**
     * 从流中读取bytes
     *
     * @param in            {@link InputStream}
     * @param isCloseStream 是否关闭输入流
     * @return bytes
     * @throws UtilException IO异常
     * @since 5.0.4
     */
    public static byte[] readBytes(InputStream in, boolean isCloseStream) throws UtilException {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        copy(in, out);
        if (isCloseStream) {
            close(in);
        }
        return out.toByteArray();
    }

    /**
     * 从Reader中读取String，读取完毕后并不关闭Reader
     *
     * @param reader Reader
     * @return String
     * @throws com.xianmao.common.exception.UtilException IO异常
     */
    public static String read(Reader reader) throws UtilException {
        final StringBuilder builder = new StringBuilder();
        final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        try {
            while (-1 != reader.read(buffer)) {
                builder.append(buffer.flip().toString());
            }
        } catch (IOException e) {
            throw new UtilException(e);
        }
        return builder.toString();
    }

    /**
     * 关闭<br>
     * 关闭失败不会抛出异常
     *
     * @param closeable 被关闭的对象
     */
    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }

    /**
     * 关闭<br>
     * 关闭失败不会抛出异常
     *
     * @param closeable 被关闭的对象
     */
    public static void close(AutoCloseable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }
}
