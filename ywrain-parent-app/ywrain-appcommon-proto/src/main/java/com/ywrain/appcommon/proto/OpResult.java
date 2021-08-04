package com.ywrain.appcommon.proto;

import java.util.List;

/**
 * 通用选项列表
 * 
 * @author xuguangming@ywrain.com
 * @date 2017年8月24日
 */
public class OpResult<T> {
    /**
     * ID
     */
    private Integer id;

    /**
     * 选项名称
     */
    private String name;

    /**
     * 图片或视频连接
     */
    private String url;

    /**
     * 选项数据列表
     */
    private List<T> oplist;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<T> getOplist() {
        return oplist;
    }

    public void setOplist(List<T> oplist) {
        this.oplist = oplist;
    }

}
