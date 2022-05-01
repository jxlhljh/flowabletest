package cn.gzsendi.flowable.listener;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gzsendi.flowable.enums.OrderStatusEnum;
import cn.gzsendi.flowable.mapper.WorkOrderMapper;
import cn.gzsendi.flowable.model.po.WorkOrder;
import cn.gzsendi.framework.utils.AppCtxHolder;
import cn.gzsendi.framework.utils.ParameterUtils;


/**
 * 通用流程的执行监听器
 * @author Mr.XiHui
 * @date 2022/04/29
 */
public class CommonFlowExecListener implements ExecutionListener {

    private static final long serialVersionUID = -1230947120947012345L;

    protected final Logger logger = LoggerFactory.getLogger(CommonFlowExecListener.class);

	@Override
	public void notify(DelegateExecution delegateExecution) {
		
        boolean infoEnabled = logger.isInfoEnabled();
        String processDefinitionId = delegateExecution.getProcessDefinitionId();
        String processInstanceId = delegateExecution.getProcessInstanceId();
        String orderId = delegateExecution.getProcessInstanceBusinessKey();
        String eventName = delegateExecution.getEventName();
        boolean isEndingProcess = EVENTNAME_END.equals(eventName);
        String currentActivityId = delegateExecution.getCurrentActivityId();
        Map<String, Object> variables = delegateExecution.getVariables();
        String classSimpleName = this.getClass().getSimpleName();
        
        if (infoEnabled) {
            logger.info("======================CommonFlowExecListener===" + classSimpleName + "==========================");

            logger.info("流程定义ID: {}", processDefinitionId);
            logger.info("流程实例ID: {}", processInstanceId);
            logger.info("业务key/工单ID: {}", orderId);
            logger.info("事件名称: {}", eventName);
            logger.info("当前活动ID: {}", currentActivityId);
            logger.info("流程实例变量: {}", variables);

            logger.info("=========================" + classSimpleName + "==========================");
        }
		
        //因为当前类不处于IOC的管理中，所以通过AppCtxHolder获取代理执行监听器列表，再根据校验结果执行它们
        Map<String, DelegateExecListener> delMap = AppCtxHolder.getBeanOfType(DelegateExecListener.class);
        
        for (Map.Entry<String, DelegateExecListener> entry : delMap.entrySet()) {
            String delegateExecListenerBeanName = entry.getKey();
            DelegateExecListener delegateExecListener = entry.getValue();

            if (delegateExecListener.validate(orderId, eventName, delegateExecution)) {
                if (infoEnabled) {
                    logger.info("({})通过校验,将执行其业务方法", delegateExecListenerBeanName);
                }
                delegateExecListener.doExecNotify(delegateExecution);
            }
        }
        
        if (isEndingProcess) {
        	
            //两种方法取最后一个任务节点

            ////与历史数据（历史表）相关的Service
            //HistoryService historyService = delegateExecution.getEngineServices().getHistoryService();
            ////创建历史任务实例查询，按任务开始时间倒序查询，取第一个任务，即最后一个任务节点
            //List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
            //        .processInstanceId(processInstanceId)
            //        .orderByTaskCreateTime()
            //        .desc()
            //        .listPage(0, 1);
            ////当前流程至少有一个任务节点，如果没有，那就是流程配置有问题
            //HistoricTaskInstance lastTask = historicTaskInstances.get(0);
        	
        	 //与任务数据（任务表）相关的Service
            TaskService taskService = AppCtxHolder.getBean(TaskService.class);
            //流程执行到此时，因为流程未完结，所以act_ru_task表中还有记录，这条记录就是最后一个任务节点
            Task lastTask = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

            String lastTaskId = lastTask.getId();
            
            //更新工单表的工单状态为已归档
            String orderStatus = (String) variables.get("orderStatus");
            updateWorkOrderStatus(orderId, orderStatus);
            if (OrderStatusEnum.FINISHED.getValue().equals(orderStatus)) {
                taskService.addComment(lastTaskId, processInstanceId, "审批结束（归档）");
            } else if (OrderStatusEnum.CANCELED.getValue().equals(orderStatus)) {
                taskService.addComment(lastTaskId, processInstanceId, "审批取消（撤单）");
            }
            
            if (infoEnabled) {
                List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstanceId);
                if (!ParameterUtils.isEmpty(processInstanceComments)) {
                    //UserMapper userMapper = AppCtxHolder.getBean(UserMapper.class);
                    for (int i = 0; i < processInstanceComments.size(); i++) {
                        Comment comment = processInstanceComments.get(i);
                        String taskId = comment.getTaskId();
                        String userId = comment.getUserId();
                        String message = comment.getFullMessage();
                        logger.info("任务节点({}),代理人({})的处理评论: {}", taskId, userId, message);
                    }
                }
            }
        	
        }
        
        
	}
	
    private void updateWorkOrderStatus(String orderId, String orderStatus) {
        
    	String userId = "admin";//这里为了测试写死成admin,正式环境改成获取当前登录好的用户//AuthUtils.getUserId();

        //流程结束了，就把工单表的状态改成完成状态，处理人置空
        WorkOrder workOrder = new WorkOrder();
        workOrder.setOrderId(orderId);
        workOrder.setOrderStatus(orderStatus);
        workOrder.setHandler("");
        workOrder.setUpdateUser(userId);
        workOrder.setUpdateTime(new Date());

        AppCtxHolder.getBean(WorkOrderMapper.class).updateWorkOrder(workOrder);
    }
    
}
