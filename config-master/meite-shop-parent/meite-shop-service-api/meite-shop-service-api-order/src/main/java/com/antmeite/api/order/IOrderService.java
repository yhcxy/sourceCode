/**
 * 功能说明:
 * 功能作者:
 * 创建日期:
 * 版权归属:每特教育|蚂蚁课堂所有 www.itmayiedu.com
 */
package com.antmeite.api.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.antmeite.code.base.ResponseBase;
import com.itmayeidu.api.entity.OrderEntity;

/**
 * 功能说明: <br>
 * 创建作者:每特教育-余胜军<br>
 * 创建时间:2018年9月24日 下午2:52:51<br>
 * 教育机构:每特教育|蚂蚁课堂<br>
 * 版权说明:上海每特教育科技有限公司版权所有<br>
 * 官方网站:www.itmayiedu.com|www.meitedu.com<br>
 * 联系方式:qq644064779<br>
 * 注意:本内容有每特教育学员共同研发,请尊重原创版权
 */
public interface IOrderService {

	/**
	 * 用户下单后调用库存服务进行扣库存
	 * 
	 * @return
	 */
	@GetMapping(value = "/addOrderAndStock")
	public ResponseBase addOrderAndStock(int i) throws Exception;

}
