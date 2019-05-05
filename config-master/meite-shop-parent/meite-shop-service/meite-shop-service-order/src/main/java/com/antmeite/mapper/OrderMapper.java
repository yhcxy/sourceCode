package com.antmeite.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import com.itmayeidu.api.entity.OrderEntity;

public interface OrderMapper {

	@Insert(value = "INSERT INTO `order` VALUES (#{id}, #{name}, #{orderCreatetime}, #{orderMoney}, #{orderState}, #{commodityId})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int addOrder(OrderEntity orderEntity);

}
