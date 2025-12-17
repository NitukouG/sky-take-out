package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 通过SetmealId 来查询setmeal_dish内容
     * */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getsetmealDishesBySetmealId(Long id);


//    删除当前setmealId中关联的菜品
    @Delete("delete from setmeal_dish where setmeal_id= #{setmealId}")
    void deleteBysetmealId(Long setmealId);

    void InsertBatch(List<SetmealDish> setmealDishes);
}
