package com.lkl.dcloud.trade.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lkl.dcloud.UserService;
import com.lkl.dcloud.trade.dao.gen.SpOrderMapper;
import com.lkl.dcloud.trade.dao.gen.bean.SpOrder;
import com.lkl.dcloud.vo.SpUserVo;

@Component
public class OrderService {
	@Autowired
	SpOrderMapper spOrderMapper;
	@Reference
	UserService userService;
	public void submitOrder(String uid){
		SpOrder spOrder = new SpOrder();
		spOrder.setCreateTime(new Date());
		spOrder.setGoodNo(getRand("GD"));
		spOrder.setOrderNo(getRand("OD"));
		spOrder.setPriceChannel(11D);
		
		SpUserVo spUserVo = userService.getSpUser(uid);
		
		spOrder.setUserDesc(spUserVo.getUserDesc());
		spOrder.setUserId(spUserVo.getUserId());
		spOrder.setUserName(spUserVo.getUserName());
		spOrderMapper.insert(spOrder);
	}
	private String getRand(String pre){
		Date date = new Date();
		return pre+date.getTime();
	}
}
