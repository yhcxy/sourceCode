package com.antmeite.feign;

import org.springframework.cloud.openfeign.FeignClient;

import com.antmeite.api.stock.StockService;

@FeignClient("antmeite-stock")
public interface StockFeign extends StockService {

}
