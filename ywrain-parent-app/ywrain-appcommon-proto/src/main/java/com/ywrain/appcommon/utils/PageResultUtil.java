package com.ywrain.appcommon.utils;

import java.util.List;

import com.ywrain.appcommon.consts.ProtoConsts;
import com.ywrain.appcommon.proto.PageResult;
import com.ywrain.appcommon.proto.PageResultWithInfo;

public class PageResultUtil {

    /**
     * 默认起始页码1
     */
    public static final int PAGE_ID = 1;

    public static int getPage_id(Integer page_id) {
        int _page_id = 1;
        if (page_id == null || page_id < 1) {
            _page_id = 1;
        } else {
            _page_id = page_id;
        }
        return _page_id;
    }

    /**
     * 获得正确起始位置值(offset)
     * 
     * @param page_id 页码
     * @param page_size 分页数
     * @return 起始位置值(offset)
     */
    public static int getOffset(Integer page_id, Integer page_size) {
        int idx = 0;
        if (page_id == null || page_id < 1) {
            idx = 0;
        } else {
            page_size = getPage_size(page_size);
            idx = (page_id - 1) * page_size;
        }
        return idx;
    }

    /**
     * 获得正确分页数
     * 
     * @param page_size 分页数
     * @return 正确分页数
     */
    public static int getPage_size(Integer page_size) {
        return getPage_size(page_size, ProtoConsts.PAGING_SIZE);
    }

    /**
     * 获得正确分页数
     * 
     * @param page_size 分页数
     * @param def 默认分页大小
     * @return 正确分页数
     */
    public static int getPage_size(Integer page_size, Integer def) {
        if (page_size == null || page_size < 1) {
            if (def != null && def > 0) {
                page_size = def;
            } else {
                page_size = ProtoConsts.PAGING_SIZE;
            }
        }
        return page_size;
    }

    /**
     * 根据总记录数(total_size)和分页数(page_size)返回总页数(total_page)
     * 
     * @param total_size 总记录数
     * @param page_size 分页数
     * @return 总页数
     */
    public static int getTotal_page(Long total_size, Integer page_size) {
        int total_page = 0;
        if (total_size != null && total_size > 0 && page_size != null && page_size > 0) {
            total_page = (int) Math.ceil((double) total_size / getPage_size(page_size));
        }
        return total_page;
    }

    /**
     * 根据总记录数(total_size)和分页数(page_size)返回总页数(total_page)
     * 
     * @param total_size 总记录数
     * @param page_size 分页数
     * @return 总页数
     */
    public static int getTotal_page(Integer total_size, Integer page_size) {
        int total_page = 0;
        if (total_size != null && total_size > 0 && page_size != null && page_size > 0) {
            total_page = getTotal_page(total_size.longValue(), page_size);
        }
        return total_page;
    }

    /**
     * 分页列表数据(不返回页码信息)
     * 
     * @param list 列表数据
     * @return 分页响应对象
     */
    public static <T> PageResult<T> paser(List<T> list) {
        return new PageResult<T>(list);
    }

    /**
     * 分页列表数据
     * 
     * @param page_id 页码
     * @param page_size 分页数
     * @param total_size 总记录数
     * @param list 列表数据
     * @return 分页响应对象
     */
    public static <T> PageResult<T> paser(Integer page_id, Integer page_size, Long total_size, List<T> list) {
        int _page_id = getPage_id(page_id);
        int _page_size = getPage_id(page_size);
        return new PageResult<T>(_page_id, _page_size, total_size, getTotal_page(total_size, _page_size), list);
    }

    /**
     * 带主体信息的分页列表数据(不返回页码信息)
     * 
     * @param info 主体信息
     * @param list 列表数据
     * @return 分页响应对象
     */
    public static <I, T> PageResultWithInfo<I, T> paser(I info, List<T> list) {
        return new PageResultWithInfo<I, T>(info, list);
    }

    /**
     * 带主体信息的分页列表数据
     * 
     * @param page_id 页码
     * @param page_size 分页数
     * @param total_size 总记录数
     * @param info 主体信息
     * @param list 列表数据
     * @return 分页响应对象
     */
    public static <I, T> PageResultWithInfo<I, T> paser(Integer page_id, Integer page_size, Long total_size, I info, List<T> list) {
        int _page_id = getPage_id(page_id);
        int _page_size = getPage_id(page_size);
        return new PageResultWithInfo<I, T>(_page_id, _page_size, total_size, getTotal_page(total_size, _page_size), info, list);
    }
}
