package cn.gzsendi.flowable.model.po;


import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.gzsendi.framework.utils.JsonUtil;

/** 
 * 工单任务节点代理人实体，表(wo_assignee)的(持久化)实体对象.
 * @author Mr.XiHui
 * @date 2019/04/28
 */
//"表(wo_assignee)的(持久化)实体对象.")
public class WorkOrderAssignee implements Serializable {
    
    private static final long serialVersionUID = -7835007305589191964L;
    
    /*** 自增ID */
    private Integer id;

    /*** 工单号. */
    //@ApiModelProperty("工单号.")
    private String orderId;

    /*** 工单任务代理人的版本号，为防止影响到已有流程而设计. */
    //@ApiModelProperty("工单任务代理人的版本号，为防止影响到已有流程而设计.")
    private Integer assigneeVersion;

    /*** 工单任务代理人的用户ID. */
    //@ApiModelProperty("工单任务代理人的用户ID.")
    private String assignee;

    /*** 工单任务代理人的顺序，从1开始，用于表示此工单任务代理人为第几个审批人/处理人. */
    //@ApiModelProperty("工单任务代理人的顺序，从1开始，用于表示此工单任务代理人为第几个审批人/处理人.")
    private Integer assigneeOrder;

    /*** 任务(节点)名. */
    //@ApiModelProperty("任务(节点)名.")
    private String taskName;

    /*** 流程执行到此任务(节点)时的工单状态. */
    //@ApiModelProperty("流程执行到此任务(节点)时的工单状态.")
    private String orderStatus;

    /*** 此工单任务代理人是否可以取消工单. */
    //@ApiModelProperty("此工单任务代理人是否可以取消工单.")
    private Boolean canCancelOrder;

    /*** 创建人. */
    //@ApiModelProperty("创建人.")
    private String createUser;

    /*** 创建时间. */
    //@ApiModelProperty("创建时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /*** 更新人. */
    //@ApiModelProperty("更新人.")
    private String updateUser;

    /*** 更新时间. */
    //@ApiModelProperty("更新时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /*** 备注. */
    //@ApiModelProperty("备注.")
    private String remark;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getAssigneeVersion() {
        return assigneeVersion;
    }

    public void setAssigneeVersion(Integer assigneeVersion) {
        this.assigneeVersion = assigneeVersion;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee == null ? null : assignee.trim();
    }

    public Integer getAssigneeOrder() {
        return assigneeOrder;
    }

    public void setAssigneeOrder(Integer assigneeOrder) {
        this.assigneeOrder = assigneeOrder;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public Boolean getCanCancelOrder() {
        return canCancelOrder;
    }

    public void setCanCancelOrder(Boolean canCancelOrder) {
        this.canCancelOrder = canCancelOrder;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        return JsonUtil.toJSONString(this);
    }
}