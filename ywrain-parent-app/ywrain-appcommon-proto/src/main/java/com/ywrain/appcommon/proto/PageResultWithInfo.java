package com.ywrain.appcommon.proto;

import java.util.List;

/**
 * 通用分页列表数据<br>
 * 分页信息和只下发list, 使用泛型方便查询好数据后续处理
 * 
 * @author xuguangming@ywrain.com
 */
public class PageResultWithInfo<I, T> {
    /**
     * 页码
     */
    private Integer page_id;
    /**
     * 分页数
     */
    private Integer page_size;
    /**
     * 总记录数
     */
    private Long total_size;
    /**
     * 总页数
     */
    private Integer total_page;
    /**
     * info数据
     */
    private I info;
    /**
     * list列表数据
     */
    private List<T> list;

    public PageResultWithInfo(Integer page_id, Integer page_size) {
        this.page_id = page_id;
        this.page_size = page_size;
    }

    public PageResultWithInfo(I info, List<T> list) {
        this.info = info;
        this.list = list;
    }

    public PageResultWithInfo(Integer page_id, Integer page_size, Long total_size, Integer total_page, List<T> list) {
        this.page_id = page_id;
        this.page_size = page_size;
        this.total_size = total_size;
        this.total_page = total_page;
        this.list = list;
    }

    public PageResultWithInfo(Integer page_id, Integer page_size, Long total_size, Integer total_page, I info) {
        this.page_id = page_id;
        this.page_size = page_size;
        this.total_size = total_size;
        this.total_page = total_page;
        this.info = info;
    }

    public PageResultWithInfo(Integer page_id, Integer page_size, Long total_size, Integer total_page, I info, List<T> list) {
        this.page_id = page_id;
        this.page_size = page_size;
        this.total_size = total_size;
        this.total_page = total_page;
        this.info = info;
        this.list = list;
    }

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

    public Long getTotal_size() {
        return total_size;
    }

    public void setTotal_size(Long total_size) {
        this.total_size = total_size;
    }

    public Integer getTotal_page() {
        return total_page;
    }

    public void setTotal_page(Integer total_page) {
        this.total_page = total_page;
    }

    public I getInfo() {
        return info;
    }

    public void setInfo(I info) {
        this.info = info;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
