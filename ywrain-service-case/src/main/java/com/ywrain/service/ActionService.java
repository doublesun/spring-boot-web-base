package com.ywrain.service;

import com.ywrain.mapper.ActionPointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActionService {

    @Autowired
    private ActionPointMapper actionPointMapper;

    @Transactional
    public void transcation(){
        actionPointMapper.updateA();

        int i=1/0;
    }
}
