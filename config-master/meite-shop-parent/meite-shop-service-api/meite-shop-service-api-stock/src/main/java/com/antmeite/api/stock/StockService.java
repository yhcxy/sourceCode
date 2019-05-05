package com.antmeite.api.stock;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.antmeite.code.base.ResponseBase;

public interface StockService {

	// 根据商品id 减库存数量
	@RequestMapping("/inventoryReduction")
	public ResponseBase inventoryReduction(@RequestParam("commodityId") Long commodityId);

}
