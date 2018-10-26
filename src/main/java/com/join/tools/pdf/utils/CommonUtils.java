package com.join.tools.pdf.utils;

import java.net.URL;

/**
 * @author Join 2018-10-25
 */
public class CommonUtils {

    public final static String UTF_8 ="UTF-8";

    /**
     * 获取工程的绝对路径
     *
     * @return
     */
    public static String getRealPath(String dirName) {
        URL url = CommonUtils.class.getClassLoader().getResource(dirName);
        if (url != null) {
            return url.getPath();
        }
        return null;
    }

    /**
     * 获取工程的绝对路径
     *
     * @return
     */
    public static String getRealPath() {
        return getRealPath("");
    }
}
