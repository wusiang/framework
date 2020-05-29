package com.xianmao.file;

import com.xianmao.string.StringPool;
import com.xianmao.utils.StringUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @ClassName FileUtil
 * @Description: 文件工具类
 * @Author wjh
 * @Data 2020-05-29 21:57
 * @Version 1.0
 */
public class FileUtil {

    /**
     * 定义允许上传的文件扩展名
     */
    private static HashMap<String, String> extMap = new HashMap<String, String>();
    private static String IS_DIR = "is_dir";
    private static String FILE_NAME = "filename";
    private static String FILE_SIZE = "filesize";

    /**
     * 图片扩展名
     */
	private static String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

    static {
        extMap.put("image", ".gif,.jpg,.jpeg,.png,.bmp,.JPG,.JPEG,.PNG");
        extMap.put("flash", ".swf,.flv");
        extMap.put("media", ".swf,.flv,.mp3,.mp4,.wav,.wma,.wmv,.mid,.avi,.mpg,.asf,.rm,.rmvb");
        extMap.put("file", ".doc,.docx,.xls,.xlsx,.ppt,.htm,.html,.txt,.zip,.rar,.gz,.bz2");
        extMap.put("allfile", ".gif,.jpg,.jpeg,.png,.bmp,.swf,.flv,.mp3,.mp4,.wav,.wma,.wmv,.mid,.avi,.mpg,.asf,.rm,.rmvb,.doc,.docx,.xls,.xlsx,.ppt,.htm,.html,.txt,.zip,.rar,.gz,.bz2");
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return String 返回后缀
     */
    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(StringPool.DOT));
    }

    /**
     * 测试文件后缀 只让指定后缀的文件上传，像jsp,war,sh等危险的后缀禁止
     *
     * @param dir      目录
     * @param fileName 文件名
     * @return 返回成功与否
     */
    public static boolean testExt(String dir, String fileName) {
        String fileExt = getFileExt(fileName);

        String ext = extMap.get(dir);
        if (StringUtil.isBlank(ext) || ext.indexOf(fileExt) == -1) {
            return false;
        }
        return true;
    }

    /**
     * 文件管理排序
     */
    public enum FileSort {

        /**
         * 大小
         */
        size,

        /**
         * 类型
         */
        type,

        /**
         * 名称
         */
        name;

        /**
         * 文本排序转换成枚举
         *
         * @param sort
         * @return
         */
        public static FileSort of(String sort) {
            try {
                return FileSort.valueOf(sort);
            } catch (Exception e) {
                return FileSort.name;
            }
        }
    }

    public static class NameComparator implements Comparator<Object> {
        @Override
        public int compare(Object a, Object b) {
            Hashtable<?, ?> hashA = (Hashtable<?, ?>) a;
            Hashtable<?, ?> hashB = (Hashtable<?, ?>) b;
            if (((Boolean) hashA.get(IS_DIR)) && !((Boolean) hashB.get(IS_DIR))) {
                return -1;
            } else if (!((Boolean) hashA.get(IS_DIR)) && ((Boolean) hashB.get(IS_DIR))) {
                return 1;
            } else {
                return ((String) hashA.get(FILE_NAME)).compareTo((String) hashB.get(FILE_NAME));
            }
        }
    }

    public static class SizeComparator implements Comparator<Object> {
        @Override
        public int compare(Object a, Object b) {
            Hashtable<?, ?> hashA = (Hashtable<?, ?>) a;
            Hashtable<?, ?> hashB = (Hashtable<?, ?>) b;
            if (((Boolean) hashA.get(IS_DIR)) && !((Boolean) hashB.get(IS_DIR))) {
                return -1;
            } else if (!((Boolean) hashA.get(IS_DIR)) && ((Boolean) hashB.get(IS_DIR))) {
                return 1;
            } else {
                if (((Long) hashA.get(FILE_SIZE)) > ((Long) hashB.get(FILE_SIZE))) {
                    return 1;
                } else if (((Long) hashA.get(FILE_SIZE)) < ((Long) hashB.get(FILE_SIZE))) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    public static class TypeComparator implements Comparator<Object> {
        @Override
        public int compare(Object a, Object b) {
            Hashtable<?, ?> hashA = (Hashtable<?, ?>) a;
            Hashtable<?, ?> hashB = (Hashtable<?, ?>) b;
            if (((Boolean) hashA.get(IS_DIR)) && !((Boolean) hashB.get(IS_DIR))) {
                return -1;
            } else if (!((Boolean) hashA.get(IS_DIR)) && ((Boolean) hashB.get(IS_DIR))) {
                return 1;
            } else {
                return ((String) hashA.get("filetype")).compareTo((String) hashB.get("filetype"));
            }
        }
    }

    public static String formatUrl(String url) {
        return url.replaceAll("\\\\", "/");
    }


    /********************************File封装********************************************************/

    /**
     * 获取File封装类
     *
     * @param file 文件
     * @return File
     */
    public static FileWrap getFile(MultipartFile file) {
        return getFile(file, "image", null, null);
    }

    /**
     * 获取File封装类
     *
     * @param file 文件
     * @param dir  目录
     * @return File
     */
    public static FileWrap getFile(MultipartFile file, String dir) {
        return getFile(file, dir, null, null);
    }

    /**
     * 获取File封装类
     *
     * @param file        文件
     * @param dir         目录
     * @param path        路径
     * @param virtualPath 虚拟路径
     * @return File
     */
    public static FileWrap getFile(MultipartFile file, String dir, String path, String virtualPath) {
        return new FileWrap(file, dir, path, virtualPath);
    }

    /**
     * 获取File封装类
     *
     * @param files 文件集合
     * @return File
     */
    public static List<FileWrap> getFiles(List<MultipartFile> files) {
        return getFiles(files, "image", null, null);
    }

    /**
     * 获取File封装类
     *
     * @param files 文件集合
     * @param dir   目录
     * @return File
     */
    public static List<FileWrap> getFiles(List<MultipartFile> files, String dir) {
        return getFiles(files, dir, null, null);
    }

    /**
     * 获取File封装类
     *
     * @param files       文件集合
     * @param path        路径
     * @param virtualPath 虚拟路径
     * @return File
     */
    public static List<FileWrap> getFiles(List<MultipartFile> files, String dir, String path, String virtualPath) {
        List<FileWrap> list = new ArrayList<>();
        for (MultipartFile file : files) {
            list.add(new FileWrap(file, dir, path, virtualPath));
        }
        return list;
    }
}
