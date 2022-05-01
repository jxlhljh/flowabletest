package cn.gzsendi.flowable.model.po;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 工作流程定义
 * @author jxlhl
 */
public class WorkFlowDef implements Serializable{
	
	private static final long serialVersionUID = 5410193335923529692L;

	/**ID*/
	private Integer id; 
	
	/**工单类型*/
	private String orderType;	
	
	/**工单名称*/
	private String orderName;
	
	/**工单单号前缀*/
	private String orderIdPrex;
	
	/**备注*/
	private String remark;
	
	/**创建时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**更新时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderIdPrex() {
		return orderIdPrex;
	}

	public void setOrderIdPrex(String orderIdPrex) {
		this.orderIdPrex = orderIdPrex;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
