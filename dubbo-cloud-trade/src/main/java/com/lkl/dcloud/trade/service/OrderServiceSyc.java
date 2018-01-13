package com.lkl.dcloud.trade.service;

import java.util.Date;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.lkl.dcloud.trade.dao.gen.SpOrderMapper;
import com.lkl.dcloud.trade.dao.gen.bean.SpOrder;
import com.lkl.dcloud.user.UserCountService;
import com.lkl.dcloud.user.UserService;
import com.lkl.dcloud.user.vo.SpOrderCountVo;
import com.lkl.dcloud.user.vo.SpUserVo;

@Component
public class OrderServiceSyc {
	@Autowired
	SpOrderMapper spOrderMapper;
	//retries=2,
	@Reference(async=true,group="*")
	UserService userServiceSyc;
	@Reference(async=true)
	UserCountService userCountServiceSyc;
	@Transactional
	public void submitSycOrder(String uid) throws Exception{
		SpOrder spOrder = new SpOrder();
		spOrder.setCreateTime(new Date());
		spOrder.setGoodNo(getRand("GD"));
		spOrder.setOrderNo(getRand("OD"));
		spOrder.setPriceChannel(11D);
		
		SpUserVo spUserVo = userServiceSyc.getSpUser(uid);//返回null
		
		Future<SpUserVo> ufuture = RpcContext.getContext().getFuture();
		
		SpOrderCountVo spOrderCountVo = new SpOrderCountVo();
		spOrderCountVo.setOrderNo(spOrder.getOrderNo());//若注释掉则会报 ConstraintViolationImpl{interpolatedMessage='订单号不能为空',
		spOrderCountVo.setUserId(uid);
		Integer fut = userCountServiceSyc.addOrderCount(spOrderCountVo);
		Future<Integer> countFuter = RpcContext.getContext().getFuture();
		
		
		spUserVo = ufuture.get();
		fut = countFuter.get();
		
		
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
