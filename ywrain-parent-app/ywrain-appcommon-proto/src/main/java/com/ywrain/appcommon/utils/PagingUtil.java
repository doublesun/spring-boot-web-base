package com.ywrain.appcommon.utils;

import java.util.List;

import com.ywrain.appcommon.consts.ProtoConsts;
import com.ywrain.appcommon.proto.PagingResult;

/**
 * 协议分页工具类
 * <p>page_id默认起始1</p>
 *
 * @author xuguangming@ywrain.com
 * @date 2017年9月2日
 **/
public class PagingUtil {

    /**
     * 根据分页ID获取分页查询的起始偏移量；
     *
     * @param pageId 当前分页数
     * @return 起始偏移值
     */
    public static int pageFrom(Integer pageId) {
        if (pageId > 0) {
            pageId--;
            return pageId * ProtoConsts.PAGING_SIZE;
        }
        return 0;
    }

    /**
     * 打包分页协议数据
     *
     * @param list 列表数据
     * @param pageId 当前分页数
     * @param pageSize 分页size
     * @param totalSize 总数量
     * @param <T> 列表数据类型
     * @return 分页返回对象
     */
    public static <T> PagingResult<T> pack(List<T> list, Integer pageId, Integer pageSize, Integer totalSize) {
        PagingResult<T> pageResult = new PagingResult<T>();
        pageResult.setList(list);
        pageResult.setPage_id(pageId);
        pageResult.setPage_size(pageSize);
        pageResult.setTotal_size(totalSize);
        return pageResult;
    }

    /**
     * 打包分页协议数据
     *
     * @param list 列表数据
     * @param pageId 当前分页数
     * @param pageSize 分页size
     * @param totalSize 总数量
     * @param <T> 列表数据类型
     * @return 分页返回对象
     */
    public static <T> PagingResult<T> pack(List<T> list, Integer pageId, Integer pageSize, Long totalSize) {
        PagingResult<T> pageResult = new PagingResult<T>();
        pageResult.setList(list);
        pageResult.setPage_id(pageId);
        pageResult.setPage_size(pageSize);
        pageResult.setTotal_size(totalSize.intValue());
        return pageResult;
    }

    /**
     * 打包分页协议数据
     *
     * @param list 列表数据
     * @param pageId 当前分页数
     * @param <T> 列表数据类型
     * @return 分页返回对象
     */
    public static <T> PagingResult<T> pack(List<T> list, Integer pageId) {
        PagingResult<T> pageResult = new PagingResult<T>();
        pageResult.setPage_id(pageId);
        pageResult.setList(list);
        return pageResult;
    }

    /**
     * 打包分页协议数据
     *
     * @param list 列表数据
     * @param <T> 列表数据类型
     * @return 分页返回对象
     */
    public static <T> PagingResult<T> pack(List<T> list) {
        PagingResult<T> pageResult = new PagingResult<T>();
        pageResult.setList(list);
        return pageResult;
    }
}
