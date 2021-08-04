package com.ywrain.appcommon.utils;

import com.ywrain.appcommon.exception.AppRuntimeException;

/**
 * APP版本处理工具类
 *
 * @author xuguangming@ywrain.com
 * @date 2017年9月2日
 **/
public class VersionUtil {

    /**
     * 转换版本号为整形值: 1010100
     * <pre>
     *      "103003" -> 103003
     *      "1.3.10" -> 103010
     *      "1.3.110" -> 103110
     *      "1.13.110" -> 113110
     *  </pre>
     *
     * @param version 字符型版本号
     * @return 整型值版本号
     */
    public static int version2int(String str) {
        if (ReqUtil.isNumber(str)) {
            return Integer.parseInt(str);
        }
        String[] vers = str.split("\\.");
        if (vers.length != 3) {
            throw new AppRuntimeException("版本号参数错误");
        }
        return Integer.parseInt(vers[0]) * 100000 + Integer.parseInt(vers[1]) * 1000 + Integer.parseInt(vers[2]);
    }

    /**
     * 转换整形值版本号为字符型："xx.xx.xxx",最大支持 99.99.999
     * <pre>
     *     103003 -> "1.3.3"
     *     103010 -> "1.3.10"
     *     113110 -> "1.13.110"
     * </pre>
     *
     * @param version 整型值版本号version
     * @return 字符型版本号"xx.xx.xxx"
     */
    public static String version2str(int version) {
        int s1 = version / 100000;
        int s2 = (version % 100000) / 1000;
        int s3 = version % 1000;
        StringBuilder sb = new StringBuilder().append(s1).append(".").append(s2).append(".").append(s3);
        return sb.toString();
    }
}
