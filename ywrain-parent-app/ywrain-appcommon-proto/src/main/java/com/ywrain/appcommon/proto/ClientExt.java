package com.ywrain.appcommon.proto;

/**
 * @description 客户端扩展信息
 * @author xuguangming@ywrain.com
 * @date 2017年11月30日
 **/
public class ClientExt {
    // 应用ID
    private String appid;
    // 推送ID
    private String pushid;
    // 设备ID
    private String cid;
    // 设备型号: "xiaomi HM2A" 或 "vivo x9"
    private String dev;
    // 平台类型: 0-未知 1-ios 2-android
    private Integer plat;
    // 系统型号
    private String os;
    // 版本
    private String ver;
    // 网络: wifi/4g/3g/2g/unknown
    private String net;
    // 渠道
    private String chan;
    // 屏幕像素高宽比: 960x480
    private String pxr;
    // 定位位置
    private Location location;
    // IP定位信息
    private String ip;
    // 时间
    private Long ts;
    
    public class Location {

        private String lat;
        private String lon;
        
        public String getLat() {
            return lat;
        }
        public void setLat(String lat) {
            this.lat = lat;
        }
        public String getLon() {
            return lon;
        }
        public void setLon(String lon) {
            this.lon = lon;
        }
        
    }
    
    public String getAppid() {
        return appid;
    }
    public void setAppid(String appid) {
        this.appid = appid;
    }
    public Long getTs() {
        return ts;
    }
    public void setTs(Long ts) {
        this.ts = ts;
    }
    public String getPushid() {
        return pushid;
    }
    public void setPushid(String pushid) {
        this.pushid = pushid;
    }
    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getDev() {
        return dev;
    }
    public void setDev(String dev) {
        this.dev = dev;
    }
    public Integer getPlat() {
        return plat;
    }
    public void setPlat(Integer plat) {
        this.plat = plat;
    }
    public String getOs() {
        return os;
    }
    public void setOs(String os) {
        this.os = os;
    }
    public String getVer() {
        return ver;
    }
    public void setVer(String ver) {
        this.ver = ver;
    }
    public String getNet() {
        return net;
    }
    public void setNet(String net) {
        this.net = net;
    }
    public String getChan() {
        return chan;
    }
    public void setChan(String chan) {
        this.chan = chan;
    }
    public String getPxr() {
        return pxr;
    }
    public void setPxr(String pxr) {
        this.pxr = pxr;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

}
