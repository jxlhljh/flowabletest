package cn.gzsendi.flowable.model.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import cn.gzsendi.framework.utils.JsonUtil;

/**
 * 工单列表的入参
 *
 * @author Mr.XiHui
 * @date 2019/11/27
 */
//@ApiModel("工单列表的入参")
public class ListWorkOrdersInVo implements Serializable {

    private static final long serialVersionUID = 8274313424318515193L;

    /*** 工单所属主体的ID. */
    //@ApiModelProperty("工单所属主体的ID.")
    private String subjectId;

    /*** 工单所属主体的类型. */
    //@ApiModelProperty("工单所属主体的类型.")
    private Integer subjectType;

    /*** tab切换标志. */
    //@ApiModelProperty("tab切换标志(我发起的:build,待处理:todo,我已处理:done,全部:all")
    private String handleFlag;

    /*** 审批摘要(模糊匹配). */
    //@ApiModelProperty("审批摘要(模糊匹配).")
    private String orderSummary;

    /*** 工单号. */
    //@ApiModelProperty("工单号.")
    private String orderId;

    /*** 工单类型. */
    //@ApiModelProperty("工单类型(传数值而不是传中文).")
    private String orderType;

    /*** 工单状态数值. */
    //@ApiModelProperty("工单状态数值.")
    private String orderStatus;

    /*** 申请人姓名(模糊匹配). */
    //@ApiModelProperty("申请人(模糊匹配).")
    private String applicantName;

    /*** 申请日期(开始). */
    //@ApiModelProperty(value = "申请日期(开始)")
    private String applicationDateBegin;

    /*** 申请日期(结束). */
    //@ApiModelProperty(value = "申请日期(结束)")
    private String applicationDateEnd;

    /*** 当前登录人的用户ID. */
    //@ApiModelProperty(value = "当前登录人的用户ID.", hidden = true)
    private String userId;

    /**
     * 页码
     */
    //@ApiModelProperty("页码")
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码从1开始")
    private Integer pageNum;

    /**
     * 每页数量
     */
    //@ApiModelProperty("每页数量")
    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量必须大于0")
    private Integer pageSize;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId == null ? null : subjectId.trim();
    }

    public Integer getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }

    public String getHandleFlag() {
        return handleFlag;
    }

    public void setHandleFlag(String handleFlag) {
        this.handleFlag = handleFlag == null ? null : handleFlag.trim();
    }

    public String getOrderSummary() {
        return orderSummary;
    }

    public void setOrderSummary(String orderSummary) {
        this.orderSummary = orderSummary == null ? null : orderSummary.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName == null ? null : applicantName.trim();
    }

    public String getApplicationDateBegin() {
        return applicationDateBegin;
    }

    public void setApplicationDateBegin(String applicationDateBegin) {
        this.applicationDateBegin = applicationDateBegin == null ? null : applicationDateBegin.trim();
    }

    public String getApplicationDateEnd() {
        return applicationDateEnd;
    }

    public void setApplicationDateEnd(String applicationDateEnd) {
        this.applicationDateEnd = applicationDateEnd == null ? null : applicationDateEnd.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return JsonUtil.toJSONString(this);
    }
}
