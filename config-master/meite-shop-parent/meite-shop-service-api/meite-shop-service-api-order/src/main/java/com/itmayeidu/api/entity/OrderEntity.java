/**
 * 功能说明:
 * 功能作者:
 * 创建日期:
 * 版权归属:每特教育|蚂蚁课堂所有 www.itmayiedu.com
 */
package com.itmayeidu.api.entity;

import java.util.Date;

import lombok.Data;

/**
 * 功能说明: <br>
 * 创建作者:每特教育-余胜军<br>
 * 创建时间:2018年10月9日 下午2:06:34<br>
 * 教育机构:每特教育|蚂蚁课堂<br>
 * 版权说明:上海每特教育科技有限公司版权所有<br>
 * 官方网站:www.itmayiedu.com|www.meitedu.com<br>
 * 联系方式:qq644064779<br>
 * 注意:本内容有每特教育学员共同研发,请尊重原创版权
 */
@Data
public class OrderEntity {

	private Long id;
	// 订单名称
	private String name;
	// 订单时间
	private Date orderCreatetime;
	// 下单金额
	private Double orderMoney;
	// 订单状态
	private int orderState;
	// 商品id
	private Long commodityId;

}
