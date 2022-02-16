package com.xianmao.common.utils;

import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
 * 文件后缀过滤器
 */
public class SuffixFileFilter implements FileFilter, Serializable {

    private static final long serialVersionUID = -3389157631240246157L;

    private final String[] suffixes;

    public SuffixFileFilter(final String suffix) {
        Assert.notNull(suffix, "The suffix must not be null");
        this.suffixes = new String[]{suffix};
    }

    public SuffixFileFilter(final String[] suffixes) {
        Assert.notNull(suffixes, "The suffix must not be null");
        this.suffixes = new String[suffixes.length];
        System.arraycopy(suffixes, 0, this.suffixes, 0, suffixes.length);
    }

    @Override
    public boolean accept(File pathname) {
        final String name = pathname.getName();
        for (final String suffix : this.suffixes) {
            if (checkEndsWith(name, suffix)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkEndsWith(final String str, final String end) {
        final int endLen = end.length();
        return str.regionMatches(true, str.length() - endLen, end, 0, endLen);
    }
}
