package com.ywrain.mapper;

import com.ywrain.entity.ActionLog;
import com.ywrain.entity.ActionLogExample;
import com.ywrain.mapper.ext.ActionLogMapperExt;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ActionLogMapper extends ActionLogMapperExt {
    long countByExample(ActionLogExample example);

    int deleteByExample(ActionLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ActionLog record);

    int insertSelective(ActionLog record);

    List<ActionLog> selectByExampleWithRowbounds(ActionLogExample example, RowBounds rowBounds);

    List<ActionLog> selectByExample(ActionLogExample example);

    ActionLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ActionLog record, @Param("example") ActionLogExample example);

    int updateByExample(@Param("record") ActionLog record, @Param("example") ActionLogExample example);

    int updateByPrimaryKeySelective(ActionLog record);

    int updateByPrimaryKey(ActionLog record);

    int insertBatch(@Param("list") List<ActionLog> list);

    int insertBatchOrUpdate(@Param("list") List<ActionLog> list);

    int insertBatchOrUpdateExclusionColumn(@Param("list") List<ActionLog> list, @Param("exclusionColumns") List<String> exclusionColumns);

    List<ActionLog> limitColumnsSelectByExample(@Param("limitColumns") Set<String> limitColumns, @Param("example") ActionLogExample example);

    ActionLog limitColumnsSelectByPrimaryKey(@Param("limitColumns") Set<String> limitColumns, @Param("id") Long id);

    int limitColumnsUpdateByExample(@Param("limitColumns") Set<String> limitColumns, @Param("record") ActionLog record, @Param("example") ActionLogExample example);

    int limitColumnsUpdateByExampleSelective(@Param("limitColumns") Set<String> limitColumns, @Param("record") ActionLog record, @Param("example") ActionLogExample example);

    int limitColumnsUpdateByPrimaryKey(@Param("limitColumns") Set<String> limitColumns, @Param("record") ActionLog record);

    int limitColumnsUpdateByPrimaryKeySelective(@Param("limitColumns") Set<String> limitColumns, @Param("record") ActionLog record);
}