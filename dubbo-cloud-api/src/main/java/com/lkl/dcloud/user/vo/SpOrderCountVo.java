package com.lkl.dcloud.user.vo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

public class SpOrderCountVo  implements Serializable {
	private static final long serialVersionUID = -1535936039916913846L;
	@NotBlank(message="用户ID不能为空")
	private String userId;
	@NotBlank(message="订单号不能为空")
	private String orderNo;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
