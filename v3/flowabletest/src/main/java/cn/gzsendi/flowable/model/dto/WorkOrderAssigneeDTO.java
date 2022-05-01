package cn.gzsendi.flowable.model.dto;

import java.io.Serializable;

import cn.gzsendi.framework.utils.JsonUtil;

/** 
 * 工单任务节点代理人数据对象.
 * 
 * @author Mr.XiHui
 * @date 2019/06/11
 */
//@ApiModel("工单任务节点代理人数据对象.")
public class WorkOrderAssigneeDTO implements Serializable {
    
    private static final long serialVersionUID = -1390581239586123598L;

    /*** 工单号. */
    //@ApiModelProperty("工单号.")
    private String orderId;

    /*** 工单任务代理人的版本号，为防止影响到已有流程而设计. */
    //@ApiModelProperty("工单任务代理人的版本号，为防止影响到已有流程而设计.")
    private Integer assigneeVersion;

    /*** 工单任务代理人的用户ID. */
    //@ApiModelProperty("工单任务代理人的用户ID.")
    private String assignee;

    /*** 工单任务代理人的姓名. */
    //@ApiModelProperty("工单任务代理人的姓名.")
    private String assigneeName;

    /*** 工单任务代理人的顺序，从1开始，用于表示此工单任务代理人为第几个审批人/处理人. */
    //@ApiModelProperty("工单任务代理人的顺序，从1开始，用于表示此工单任务代理人为第几个审批人/处理人.")
    private Integer assigneeOrder;

    /*** 任务(节点)名. */
    //@ApiModelProperty("任务(节点)名.")
    private String taskName;

    /*** 流程执行到此任务(节点)时的工单状态. */
    //@ApiModelProperty("流程执行到此任务(节点)时的工单状态.")
    private String orderStatus;

    /*** 流程执行到此任务(节点)时的工单状态名称. */
    //@ApiModelProperty("流程执行到此任务(节点)时的工单状态名称.")
    private String orderStatusName;

    /*** 此工单任务代理人是否可以取消工单. */
    //@ApiModelProperty("此工单任务代理人是否可以取消工单.")
    private Boolean canCancelOrder;

    /*** 备注. */
    //@ApiModelProperty("备注.")
    private String remark;

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

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName == null ? null : assigneeName.trim();
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

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName == null ? null : orderStatusName.trim();
    }

    public Boolean getCanCancelOrder() {
        return canCancelOrder;
    }

    public void setCanCancelOrder(Boolean canCancelOrder) {
        this.canCancelOrder = canCancelOrder;
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