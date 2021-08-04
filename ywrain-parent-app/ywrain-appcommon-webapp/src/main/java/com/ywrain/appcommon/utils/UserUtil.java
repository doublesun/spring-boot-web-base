package com.ywrain.appcommon.utils;

/**
 * 用户信息工具类，用于用户UID、检查游客等处理
 * 
 * @author xuguangming@ywrain.com
 * @date 2017年9月2日
 **/
public class UserUtil {

    public final static String VISITOR_UID = "visitor";

    /**
     * 检查Uid是否是游客
     *
     * @param uid 用户UID
     * @return {@code true} 表示游客
     */
    public static boolean isVisitor(String uid) {
        return (uid == null || uid.trim().length() == 0 || uid.contains("visit"));
    }

    /**
     * 检查uid是否合法的UID
     *
     * @param uid 用户UID
     * @return {@code true} 表示合法的Uid格式
     */
    public static boolean isValidUid(String uid) {
        return (uid != null && uid.trim().length() > 0);
    }

    /**
     * 检查uid是否合法，且是有车以后APP账号体系下的统一账号UID
     *
     * @param uid 用户UID
     * @return {@code true} 表示合法统一账号UID
     */
    public static boolean isValidIycUid(String uid) {
        return (uid != null && uid.trim().length() > 0 && uid.indexOf("_") == 1);
    }
}
