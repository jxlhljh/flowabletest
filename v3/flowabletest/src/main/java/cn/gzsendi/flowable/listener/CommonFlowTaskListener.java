package cn.gzsendi.flowable.listener;

import java.util.Date;
import java.util.Map;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gzsendi.flowable.mapper.WorkOrderMapper;
import cn.gzsendi.flowable.model.po.WorkOrder;
import cn.gzsendi.framework.utils.AppCtxHolder;

/**
 * 通用流程任务监听器
 * <p>
 * CEAAF=CommonExamineAndApproveFlow
 * <p>
 * 0、  startCEAAF
 * 1、  examAndApprove
 * 2、  modification
 * 3、  endCEAAF
 * 4、  cancelCEAAF
 *
 * @author Mr.XiHui
 * @date 2019/6/12
 */
public class CommonFlowTaskListener implements TaskListener {

    private static final long serialVersionUID = -9018236501850831510L;

    protected final Logger logger = LoggerFactory.getLogger(CommonFlowTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {

        boolean infoEnabled = logger.isInfoEnabled();
        String processDefinitionId = delegateTask.getProcessDefinitionId();
        String processInstanceId = delegateTask.getProcessInstanceId();
        String taskId = delegateTask.getId();
        String taskName = delegateTask.getName();
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String taskAssignee = delegateTask.getAssignee();
        String taskEventName = delegateTask.getEventName();
        String executionId = delegateTask.getExecutionId();
        Map<String, Object> variables = delegateTask.getVariables();
        String orderId = variables.get("businessKey").toString();

        if (infoEnabled) {
            String classSimpleName = this.getClass().getSimpleName();
            logger.info("=========================" + classSimpleName + "==========================");

            String taskAssigneeName = taskAssignee;

            logger.info("流程定义ID: {}", processDefinitionId);
            logger.info("流程实例ID: {}", processInstanceId);
            logger.info("流程任务ID: {}", taskId);
            logger.info("流程任务名称: {}", taskName);
            logger.info("流程任务定义key: {}", taskDefinitionKey);
            logger.info("流程任务委派人userId: {}", taskAssignee);
            logger.info("流程任务委派人姓名: {}", taskAssigneeName);
            logger.info("流程任务事件名: {}", taskEventName);
            logger.info("流程执行ID: {}", executionId);
            logger.info("业务key/工单ID: {}", orderId);
            logger.info("流程实例变量: {}", variables);

            logger.info("=========================" + classSimpleName + "==========================");
        }

        //根据所执行到的流程任务更新工单表的工单状态
        if (EVENTNAME_CREATE.equals(taskEventName)) {
            String orderStatus = (String) variables.get("orderStatus");
            updateWorkOrderStatus(orderId, orderStatus, taskAssignee);
        }

        //因为当前类不处于IOC的管理中，所以通过AppCtxHolder获取代理执行监听器列表，再根据校验结果执行它们
        Map<String, DelegateTaskListener> dtlMap = AppCtxHolder.getBeanOfType(DelegateTaskListener.class);

        for (Map.Entry<String, DelegateTaskListener> entry : dtlMap.entrySet()) {
            String delegateTaskListenerBeanName = entry.getKey();
            DelegateTaskListener delegateTaskListener = entry.getValue();

            if (delegateTaskListener.validate(orderId, taskDefinitionKey, taskEventName, delegateTask)) {
                if (infoEnabled) {
                    logger.info("({})通过校验,将执行其业务方法", delegateTaskListenerBeanName);
                }
                delegateTaskListener.doTaskNotify(delegateTask);
            }
        }
    }

    /**
     * 处理工单状态
     *
     * @param orderId     工单ID
     * @param orderStatus 当前工单状态
     * @param handler     当前处理人的用户ID
     */
    private void updateWorkOrderStatus(String orderId, String orderStatus, String handler) {
    	String userId = "admin";//这里为了测试写死成admin,正式环境改成获取当前登录好的用户//AuthUtils.getUserId();

        WorkOrder workOrder = new WorkOrder();
        workOrder.setOrderId(orderId);
        workOrder.setOrderStatus(orderStatus);
        workOrder.setHandler(handler);
        workOrder.setUpdateUser(userId);
        workOrder.setUpdateTime(new Date());

        AppCtxHolder.getBean(WorkOrderMapper.class).updateWorkOrder(workOrder);
    }
}

