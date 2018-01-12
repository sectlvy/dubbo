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
	//retries=2,
	@Reference(check=false,cluster="forking",loadbalance="roundrobin")//启动检查关闭，该为测试时使用，服务启动时默认用户服务已经存在 否则会报错
									 //retries 失败重试2次
									 //cluster forking 表示并行请求
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
