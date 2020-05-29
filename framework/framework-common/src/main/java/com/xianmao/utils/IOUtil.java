package com.xianmao.utils;

import java.io.*;

/**
 * @ClassName IOUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 10:19
 * @Version 1.0
 */
public class IOUtil {

    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static void copy(InputStream is, OutputStream os) {

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
}
