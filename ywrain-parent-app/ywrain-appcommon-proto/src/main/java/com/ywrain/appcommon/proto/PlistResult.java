package com.ywrain.appcommon.proto;

import java.util.List;

/**
 * 通用列表数据返回
 * 
 * @author xuguangming@ywrain.com
 * @date 2017年3月24日
 */
public class PlistResult<E> {

    /**
     * 页面基本信息
     */
    private List<E> list;

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
}
