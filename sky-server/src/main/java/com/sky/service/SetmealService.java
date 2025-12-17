package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    void updateWithsetmealdish(SetmealDTO setmealDTO);

    SetmealVO getById(Long id);

    PageResult pageQuery(SetmealPageQueryDTO pageQueryDTO);

    void stopOrStart(Integer status, Long id);

    void deleteByIds(List<Long> ids);

    void InsertBatch(SetmealDTO setmealDTO);
}
