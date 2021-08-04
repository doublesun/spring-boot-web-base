package com.ywrain.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * IP提取相关的工具类
 *
 * @author weipengfei@youcheyihou.com
 * @since 1.2.0
 */
public class IpUtil {
    public final static String LOCAL_IP = "127.0.0.1";
    // IP v4
    public final static Pattern IPV4 = Pattern.compile(
        "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    // IP v6
    public final static Pattern IPV6 = Pattern.compile(
        "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))");

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }

    /**
     * 获取节点所在本机默认网卡IP 针对linux机器，(不返回loopback的IP)，一般是局域网内IP windows机器尚未测试
     *
     * @return String IP字符串:192.168.0.10
     */
    public static String getLocalNetworkIp() {
        Enumeration<NetworkInterface> allNetInterfaces = null;
        String ip = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                String netInterfaceName = netInterface.getName();
                if (netInterfaceName == null) {
                    continue;
                }
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ipAddr = (InetAddress) addresses.nextElement();
                    if (ipAddr == null || !(ipAddr instanceof Inet4Address)) {
                        continue;
                    }
                    // 排除loopback类型地址
                    if (ipAddr.isLoopbackAddress()) {
                        continue;
                    }
                    // 如果是site-local地址
                    if (ipAddr.isSiteLocalAddress()) {
                        return ipAddr.getHostAddress();
                    }

                    if ("eth0".equals(netInterfaceName)) {
                        // linux-普通网卡
                        return ipAddr.getHostAddress();
                    } else if ("en0".equals(netInterfaceName)) {
                        // mac-普通网卡
                        return ipAddr.getHostAddress();
                    } else if ("enp3s0".equals(netInterfaceName)) {
                        // 特殊网卡
                        return ipAddr.getHostAddress();
                    }
                    // docker虚拟节点的IP可能是动态的，暂不予考虑
                }
            }

            // 其他情况，linux下可能会拿到loopback地址(127.x.x.x)
            InetAddress inet = InetAddress.getLocalHost();
            ip = inet.getHostAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ip;
    }

    /**
     * 根据long值获取ip v4地址
     *
     * @param longIP IP的long表示形式
     * @return IP V4 地址
     */
    public static String longToIpv4(long longIP) {
        final StringBuilder sb = new StringBuilder();
        // 直接右移24位
        sb.append(String.valueOf(longIP >>> 24));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(longIP & 0x000000FF));
        return sb.toString();
    }

    /**
     * 根据ip地址计算出long型的数据
     *
     * @param strIP IP V4 地址
     * @return long值
     */
    public static long ipv4ToLong(String strIP) {
        if (RexUtil.isMatch(IPV4, strIP)) {
            long[] ip = new long[4];
            // 先找到IP地址字符串中.的位置
            int position1 = strIP.indexOf(".");
            int position2 = strIP.indexOf(".", position1 + 1);
            int position3 = strIP.indexOf(".", position2 + 1);
            // 将每个.之间的字符串转换成整型
            ip[0] = Long.parseLong(strIP.substring(0, position1));
            ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
            ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
            ip[3] = Long.parseLong(strIP.substring(position3 + 1));
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        }
        return 0;
    }

    /**
     * 判断指定的IP字符串是否是有效IP
     *
     * @param strIP IP字符串
     * @return TRUE | FALSE
     */
    public static boolean isValidIP(String strIP) {
        return isIPV4(strIP) || isIPV6(strIP);
    }

    /**
     * 判断指定的IP字符串是否是IPV4
     *
     * @param strIP IP字符串
     * @return TRUE | FALSE
     */
    public static boolean isIPV4(String strIP) {
        if (strIP != null) {
            return RexUtil.isMatch(IPV4, strIP);
        }
        return false;
    }

    /**
     * 判断指定的IP字符串是否是IPV6
     *
     * @param strIP IP字符串
     * @return TRUE | FALSE
     */
    public static boolean isIPV6(String strIP) {
        if (strIP != null) {
            return RexUtil.isMatch(IPV6, strIP);
        }
        return false;
    }

    /**
     * 判定是否为内网IP<br> 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
     *
     * @param strIP IP地址
     * @return 是否为内网IP
     */
    public static boolean isInnerIP(String strIP) {
        boolean isInnerIp = false;
        long ipNum = IpUtil.ipv4ToLong(strIP);

        long aBegin = IpUtil.ipv4ToLong("10.0.0.0");
        long aEnd = IpUtil.ipv4ToLong("10.255.255.255");

        long bBegin = IpUtil.ipv4ToLong("172.16.0.0");
        long bEnd = IpUtil.ipv4ToLong("172.31.255.255");

        long cBegin = IpUtil.ipv4ToLong("192.168.0.0");
        long cEnd = IpUtil.ipv4ToLong("192.168.255.255");

        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || strIP.equals(LOCAL_IP);
        return isInnerIp;
    }
}
