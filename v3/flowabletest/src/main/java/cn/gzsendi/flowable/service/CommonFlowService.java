package cn.gzsendi.flowable.service;

import java.util.List;
import java.util.Map;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;

import cn.gzsendi.flowable.enums.TaskResultEnum;

public interface CommonFlowService {
	
	/**
	 * 通用审批流程服务
	 * <p>
	 * CEAAF=CommonExamineAndApproveFlow
	 * <p>
	 * 0、  startCEAAF
	 * 1、  submitApplication
	 * 2、  examAndApprove
	 * 2.1、firstModification
	 * 3、  finalExamAndApprove
	 * 3.1、secondModification
	 * 4、  handle
	 * 4.1、thirdModification
	 * 5、  archive
	 * 6、  endCEAAF
	 *
	 * @author liujinghua
	 * @date 2022/04/29
	 */
	
    /**
     * 开启一个通用的审批流程
     *
     * @param businessKey      唯一业务key，打开流程后可以通过此key查询流程实例，要求必须全局唯一
     * @param applicant        申请人
     * @param assigneesOrderId 获取任务节点列表(代理人列表)的工单号
     *                         说明：wo_assignee表以orderId为主键，
     *                         待开启的流程的任务节点列表(代理人列表)
     *                         将按主键分别为orderId、assigneesOrderId、orderType三种顺序查找
     *                         先查select * from wo_assignee where orderId = #{orderId} and ...的情况
     *                         如无再查select * from wo_assignee where orderId = #{assigneesOrderId} and ...的情况
     *                         再无最后查select * from wo_assignee where orderId = #{orderType} and ...的情况
     * @param variables        流程实例初始流程变量，会在`act_hi_varinst`表中保存
     * @return 流程实例对象
     */
    ProcessInstance startCommonFlow(String businessKey, String applicant,
                                    String assigneesOrderId, Map<String, Object> variables);

    /**
     * 审批申请
     *
     * @param businessKey   唯一业务key
     * @param eaaResultEnum 审批结果枚举
     *                      result >= 0  ：通过，交给下一个人再审或没有下一个审批人时则归档结束流程
     *                      result == -999 ：不通过，直接取消审批(撤单)
     *                      result < 0 && result != -999 ：不通过，下一步修改申请
     * @param variables     流程实例流程变量，运行时会覆盖之前任务的值
     * @param comment       备注信息
     */
    void examAndApprove(String businessKey, TaskResultEnum eaaResultEnum,
                        Map<String, Object> variables, String comment);

    /**
     * 审批不通过时，如果不是直接取消审批，则进行修改，然后再从初审开始重批
     *
     * @param businessKey      唯一业务key
     * @param modifyResultEnum 修改结果枚举
     *                         result == -999 ：不通过，直接取消审批(撤单)
     *                         result != -999 ：修改完成，下一步初审
     * @param variables        流程实例流程变量，运行时会覆盖之前任务的值
     * @param comment          备注信息
     */
    void doModification(String businessKey, TaskResultEnum modifyResultEnum,
                        Map<String, Object> variables, String comment);

    /**
     * 判断当前流程是否待第一个审批人进行处理
     *
     * @param businessKey 唯一业务key
     * @param currentTask 当前任务节点，可传null，传null时将自动通过businessKey查询再使用
     * @return boolean
     */
    boolean isTheFirstAssigneeNow(String businessKey, Task currentTask);

    Task getCurrentTaskByBusinessKey(String businessKey);

    String getBusinessKeyByTaskId(String taskId);

    String getCurrentTaskDefinitionKey(String businessKey);

    /**
     * 逻辑删除流程实例
     *
     * @param businessKey  唯一业务key
     * @param deleteReason 删除原因
     */
    void deleteFlowLogically(String businessKey, String deleteReason);

    /**
     * 物理删除流程实例
     * @param businessKey  唯一业务key
     */
    void deleteFlowPhysically(String businessKey);

    /**
     * 进行一个完整的流程测试
     * @param businessKey
     * @return
     */
    int testCommonExamineAndApproveFlow(String businessKey);
    
    /**
     * 流程任务批注列表
     * @param businessKey 唯一业务key
     * @return 批注列表
     */
    List<Comment> listTaskComments(String businessKey);

}
