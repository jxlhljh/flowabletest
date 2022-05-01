package cn.gzsendi.flowable.model.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.gzsendi.framework.utils.JsonUtil;

/**
 * 流程任务批注数据对象
 *
 * @author Mr.XiHui
 * @date 2019/6/17
 */
//"流程任务批注")
public class TaskCommentDTO implements Serializable {

    private static final long serialVersionUID = 6600398420646666089L;

    /*** 任务批注ID */
    //"任务批注ID")
    private String taskCommentId;

    /*** 任务ID */
    //"任务ID")
    private String taskId;

    /*** 流程实例ID */
    //"流程实例ID")
    private String processInstanceId;

    /*** 工单号 */
    //"工单号")
    private String businessKey;

    /*** 用户ID */
    //"用户ID")
    private String userId;

    /*** 用户姓名 */
    //"用户姓名")
    private String userName;

    /*** 用户头像 */
    //"用户头像")
    private String avatar;

    /*** 批注信息 */
    //"批注信息")
    private String message;

    /*** 批注时间 */
    //"批注时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date commentTime;

    public String getTaskCommentId() {
        return taskCommentId;
    }

    public void setTaskCommentId(String taskCommentId) {
        this.taskCommentId = taskCommentId == null ? null : taskCommentId.trim();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId == null ? null : processInstanceId.trim();
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey == null ? null : businessKey.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return JsonUtil.toJSONString(this);
    }
}
