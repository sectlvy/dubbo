package com.lkl.dcloud.user.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.lkl.dcloud.UserService;
import com.lkl.dcloud.user.dao.gen.SpUserMapper;
import com.lkl.dcloud.user.dao.gen.bean.SpUser;
import com.lkl.dcloud.vo.SpUserVo;

@Service(timeout = 5000,group="u-public")
@Component
public class UserServiceImpl implements UserService {
	@Autowired
	SpUserMapper spUserMapper;
	public SpUserVo getSpUser(String id) {
		SpUser spUser =  spUserMapper.selectByPrimaryKey(id);
		SpUserVo spUserVo = new SpUserVo();
		try {
			TimeUnit.SECONDS.sleep(4);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		BeanUtils.copyProperties(spUser, spUserVo);
		return spUserVo;
	}

}
