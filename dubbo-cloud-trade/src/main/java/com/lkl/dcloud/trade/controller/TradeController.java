package com.lkl.dcloud.trade.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lkl.dcloud.trade.dao.gen.bean.SpOrder;
import com.lkl.dcloud.trade.service.OrderService;

@RequestMapping("trade")
@RestController
public class TradeController {
	@Autowired
	OrderService orderService;
	@RequestMapping(value = "v1.0/submitOrder", method = {RequestMethod.GET,RequestMethod.POST}, produces = "application/json; charset=utf-8")
	public String findBankCardList(String uid, HttpServletRequest request) {
		SpOrder spOrder = orderService.submitOrder(uid);
		return JSON.toJSONString(spOrder);
	}
}
