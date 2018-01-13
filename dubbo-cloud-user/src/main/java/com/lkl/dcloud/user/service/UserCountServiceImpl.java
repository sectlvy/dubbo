package com.lkl.dcloud.user.service;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.lkl.dcloud.user.UserCountService;
import com.lkl.dcloud.user.dao.gen.SpUserCountMapper;
import com.lkl.dcloud.user.dao.gen.bean.SpUserCount;
import com.lkl.dcloud.user.vo.SpOrderCountVo;

@Service(validation="true")
@Component
public class UserCountServiceImpl implements UserCountService {
	@Autowired
	SpUserCountMapper spUserCountMapper;
	@Override
	public Integer addOrderCount(SpOrderCountVo spOrderCountVo) {
		SpUserCount spUserCount = new SpUserCount();
		spUserCount.setCreateTime(new Date());
		spUserCount.setUserId(spOrderCountVo.getUserId());
		
		SpUserCount spUserCountDb = spUserCountMapper.selectByPrimaryKey(spOrderCountVo.getUserId());
		if(spUserCountDb==null){
			spUserCount.setOrderCount(1);
			spUserCountMapper.insert(spUserCount);
		}else{
			spUserCount.setOrderCount(spUserCountDb.getOrderCount()+1);
			spUserCountMapper.updateByPrimaryKeySelective(spUserCount);
		}
		return spUserCountMapper.selectByPrimaryKey(spOrderCountVo.getUserId()).getOrderCount();
	}

}
