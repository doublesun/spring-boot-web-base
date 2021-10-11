package com.ywrain.mapper;

import com.ywrain.entity.ActionPoint;
import com.ywrain.entity.ActionPointExample;
import com.ywrain.mapper.ext.ActionPointMapperExt;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ActionPointMapper extends ActionPointMapperExt {
    long countByExample(ActionPointExample example);

    int deleteByExample(ActionPointExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActionPoint record);

    int insertSelective(ActionPoint record);

    List<ActionPoint> selectByExampleWithRowbounds(ActionPointExample example, RowBounds rowBounds);

    List<ActionPoint> selectByExample(ActionPointExample example);

    ActionPoint selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActionPoint record, @Param("example") ActionPointExample example);

    int updateByExample(@Param("record") ActionPoint record, @Param("example") ActionPointExample example);

    int updateByPrimaryKeySelective(ActionPoint record);

    int updateByPrimaryKey(ActionPoint record);

    int insertBatch(@Param("list") List<ActionPoint> list);

    int insertBatchOrUpdate(@Param("list") List<ActionPoint> list);

    int insertBatchOrUpdateExclusionColumn(@Param("list") List<ActionPoint> list, @Param("exclusionColumns") List<String> exclusionColumns);

    List<ActionPoint> limitColumnsSelectByExample(@Param("limitColumns") Set<String> limitColumns, @Param("example") ActionPointExample example);

    ActionPoint limitColumnsSelectByPrimaryKey(@Param("limitColumns") Set<String> limitColumns, @Param("id") Long id);

    int limitColumnsUpdateByExample(@Param("limitColumns") Set<String> limitColumns, @Param("record") ActionPoint record, @Param("example") ActionPointExample example);

    int limitColumnsUpdateByExampleSelective(@Param("limitColumns") Set<String> limitColumns, @Param("record") ActionPoint record, @Param("example") ActionPointExample example);

    int limitColumnsUpdateByPrimaryKey(@Param("limitColumns") Set<String> limitColumns, @Param("record") ActionPoint record);

    int limitColumnsUpdateByPrimaryKeySelective(@Param("limitColumns") Set<String> limitColumns, @Param("record") ActionPoint record);
}