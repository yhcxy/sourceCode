/**
 * 功能说明:
 * 功能作者:
 * 创建日期:
 * 版权归属:每特教育|蚂蚁课堂所有 www.itmayiedu.com
 */
package com.antmeite.api.order.impl;

import com.antmeite.api.order.IOrderService;
import com.antmeite.code.base.BaseApiService;
import com.antmeite.code.base.ResponseBase;
import com.antmeite.feign.StockFeign;
import com.antmeite.mapper.OrderMapper;
import com.codingapi.tx.annotation.TxTransaction;
import com.itmayeidu.api.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 功能说明: <br>
 * 创建作者:每特教育-余胜军<br>
 * 创建时间:2018年9月24日 下午2:58:07<br>
 * 教育机构:每特教育|蚂蚁课堂<br>
 * 版权说明:上海每特教育科技有限公司版权所有<br>
 * 官方网站:www.itmayiedu.com|www.meitedu.com<br>
 * 联系方式:qq644064779<br>
 * 注意:本内容有每特教育学员共同研发,请尊重原创版权
 */
@RestController
public class IOrderServiceImpl extends BaseApiService implements IOrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private StockFeign stockFeign;

	// 下单扣库存

	/**
	 * 使用@TxTransaction解决分布式事务 isStart true 是:是发起方 false 否:是参与方
	 * 同理生产者 同样配置集成即可，只需要把Transaction 中的isStart改为fasle即可。
	 * @param i
	 * @return
	 * @throws Exception
	 */
    @TxTransaction(isStart = true)
	@Transactional
	@GetMapping(value = "/addOrderAndStock")
	public ResponseBase addOrderAndStock(int i) throws Exception {
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setName("蚂蚁课堂永久会员充值");
		orderEntity.setOrderCreatetime(new Date());
		// 价格是300元
		orderEntity.setOrderMoney(300d);
		// 状态为 未支付
		orderEntity.setOrderState(0);
		Long commodityId = 30l;
		// 商品id
		orderEntity.setCommodityId(commodityId);
		// 1.先下单，创建订单
		int orderResult = orderMapper.addOrder(orderEntity);
		System.out.println("orderResult:" + orderResult);
		if (orderResult <= 0) {
			return setResultError("下单失败!");
		}

		// 2.下单成功后,调用库存服务
		ResponseBase inventoryReduction = stockFeign.inventoryReduction(commodityId);
		if (inventoryReduction.getRtnCode() != 200) {
			// 1.使用手动事务 -
			// TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 2.获取将异常抛出给上一层，外面回滚。
			throw new Exception("调用库存服务接口失败，开始回退订单事务代码");
		}
		int reuslt = 1 / i;
		return setResultSuccess("下单成功!");
	}

}
