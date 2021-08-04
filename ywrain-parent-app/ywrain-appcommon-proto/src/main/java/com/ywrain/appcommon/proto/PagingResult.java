package com.ywrain.appcommon.proto;

import java.util.List;

/**
 * 通用分页列表数据，APP客户端一般只需要下发list
 *
 * @author xuguangming@ywrain.com
 * @date 2017年8月24日
 */
public class PagingResult<T> {

    /**
     * 当前页面ID，默认1-~
     */
    private Integer page_id;

    /**
     * 每页数
     */
    private Integer page_size;

    /**
     * 总数
     */
    private Integer total_size;

    /**
     * 列表数据
     */
    private List<T> list;

    public Integer getPage_id() {
        return page_id;
    }

    public void setPage_id(Integer page_id) {
        this.page_id = page_id;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Integer getTotal_size() {
        return total_size;
    }

    public void setTotal_size(Integer total_size) {
        this.total_size = total_size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
