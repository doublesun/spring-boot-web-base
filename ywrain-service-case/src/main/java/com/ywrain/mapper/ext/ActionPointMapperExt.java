package com.ywrain.mapper.ext;

import org.apache.ibatis.annotations.Update;

public interface ActionPointMapperExt {

    @Update("update action_log set del=1 where id=1384045526598496257")
    void updateA();
}