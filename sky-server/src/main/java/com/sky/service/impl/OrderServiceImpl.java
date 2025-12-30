package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.controller.user.ShoppingCartController;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Autowired
    AddressBookMapper addressBookMapper;

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

//        处理各种异常(地址 和 购物车)
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

//        购物车
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

//      向订单表插入一条数据

        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);

        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setUserId(currentId);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setOrderTime(LocalDateTime.now());
//        设置是由谁提交的
        orders.setUserId(currentId);

        orderMapper.insert(orders);
//        向订单明细表插入n条数据
//        对 stream（流）中的每个元素执行“处理逻辑”，并返回一个新的元素。
        List<OrderDetail> orderDetails = shoppingCartList.stream().map(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());  // 设置订单明细ID
            return orderDetail;
        }).collect(Collectors.toList());

        orderDetailMapper.insertBatch(orderDetails);
//
//        清空购物车
        shoppingCartMapper.clean(currentId);
//
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount()).build();


        return orderSubmitVO;
    }

    @Override
    public OrderVO queryOrderDetail(Long id) {
//        两步
//        第一步 查找 orders
        Orders orders = orderMapper.getById(id);
//        第二步 查找OrdersDetail
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orders.getId());

//        组装
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);

        return orderVO;
    }

    @Override
    public PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 设置分页
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
//        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
//        ordersPageQueryDTO.setStatus(status);

        // 分页条件查询
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);

//        return new PageResult(page.getTotal(), collect);
    }

    @Override
    public void cancelOrder(Long id) {
//        取消订单只要
//        将status设置为6 肯定是需要本人取消的
//        因此
//        Orders order = orderMapper.getById(id);
        Long currentId = BaseContext.getCurrentId();

//        if (order.getUserId() == null || !order.getUserId().equals(currentId)){
//            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
//        }

//        接下来就是更新账单信息
        Orders orders = new Orders();
        orders.setId(id);
        orders.setUserId(currentId);
        orders.setStatus(Orders.CANCELLED);

        orderMapper.update(orders);


    }

    @Override
    public void repetitionOrder(Long id) {
//        再来一单的逻辑
//        第一个 查你当前的订单id
//        保证当前order 中的订单和当前登录用户的一致
        Orders order = orderMapper.getById(id);
        Long currentId = BaseContext.getCurrentId();
        if (order == null || !order.getUserId().equals(currentId)){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
//        Long orderId = id;
//        查你当前的orderDetail
        List<OrderDetail> OrderDetails = orderDetailMapper.getByOrderId(id);
//
////        重新插入到orders中
//        order.setId(null);
//        orderMapper.insert(order);
//
////        换个order_id重新插入到orderDetail 中
//        List<OrderDetail> collect = OrderDetails.stream().peek(orderDetail -> {
//            orderDetail.setOrderId(order.getId());  // 插入新的orderId
//        }).collect(Collectors.toList());
//
////        插入orderDetail
//        orderDetailMapper.insertBatch(collect);

//        只要重新加入到购物车中就可以
        List<ShoppingCart> collect = OrderDetails.stream().map(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(currentId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        shoppingCartMapper.insertBatch(collect);

    }
}
