package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);

    @Select("select * from orders where id =#{id}")
    Orders getById(Long id);
    @Select("select * from orders where user_id =#{userId}")
    Page<OrdersDTO> getByUserId(Long userId);

//    Page<Orders> getPageResult(OrdersPageQueryDTO ordersPageQueryDTO);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    void update(Orders orders);
}
