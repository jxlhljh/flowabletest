package cn.gzsendi.flowable.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.gzsendi.flowable.enums.OrderStatusEnum;
import cn.gzsendi.flowable.enums.ProcessDefinitionKeyEnum;
import cn.gzsendi.flowable.enums.TaskDefinitionKeyEnum;
import cn.gzsendi.flowable.enums.TaskResultEnum;
import cn.gzsendi.flowable.mapper.FlowableMapper;
import cn.gzsendi.flowable.mapper.WorkFlowDefMapper;
import cn.gzsendi.flowable.mapper.WorkOrderAssigneeMapper;
import cn.gzsendi.flowable.model.po.WorkFlowDef;
import cn.gzsendi.flowable.model.po.WorkOrderAssignee;
import cn.gzsendi.flowable.service.CommonFlowService;
import cn.gzsendi.framework.exception.GzsendiException;
import cn.gzsendi.framework.utils.ParameterUtils;

/**
 * 通用审批流程服务实现
 * <p>
 * CEAAF=CommonExamineAndApproveFlow
 * <p>
 * 0、 startCEAAF 1 <= n <= e-1、 examAndApprove （循环审批，直到没有下一个审批节点） 2 <= m <= n+1、
 * modification （某个审批节点不通过则进入修改节点，完成修改节点后回到第一个审批节点继续批） e、 endCEAAF
 * @author liujinghua
 * @date 2022/4/30
 */
@Service
public class CommonFlowServiceImpl implements CommonFlowService {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private WorkOrderAssigneeMapper workOrderAssigneeMapper;
	
	@Autowired
	private FlowableMapper flowableMapper;
	
	@Autowired
	private WorkFlowDefMapper workFlowDefMapper;

	/**
	 * 开启一个通用的审批流程
	 *
	 * @param businessKey
	 *            唯一业务key，打开流程后可以通过此key查询流程实例，要求必须全局唯一
	 * @param applicant
	 *            申请人
	 * @param assigneesOrderId
	 *            获取任务节点列表(代理人列表)的工单号 说明：wo_assignee表以orderId为主键，
	 *            待开启的流程的任务节点列表(代理人列表)
	 *            将按主键分别为orderId、assigneesOrderId、orderType三种顺序查找 先查select *
	 *            from wo_assignee where orderId = #{orderId} and ...的情况
	 *            如无再查select * from wo_assignee where orderId =
	 *            #{assigneesOrderId} and ...的情况 再无最后查select * from wo_assignee
	 *            where orderId = #{orderType} and ...的情况
	 * @param variables
	 *            流程实例初始流程变量，会在`act_hi_varinst`表中保存
	 * @return 流程实例对象
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ProcessInstance startCommonFlow(String businessKey, String applicant, String assigneesOrderId,
			Map<String, Object> variables) {

		if (!StringUtils.hasText(businessKey)) {
			throw new RuntimeException("开启审批流程时，唯一业务key不能为空");
		}
		if (!StringUtils.hasText(applicant)) {
			throw new RuntimeException("开启审批流程时，申请人不能为空");
		}

		if (variables == null || variables.size() == 0) {
			variables = new HashMap<>();
		} else {
			variables = new HashMap<>(variables);
		}

		// 流程定义时使用applicant为申请人变量
		variables.put("applicant", applicant);
		variables.put("assigneesOrderId", assigneesOrderId);
		variables.put("businessKey", businessKey);

		// 调用此方法设置流程操作用户线程变量，主要是供taskService.addComment方法内部逻辑使用，这个用户ID将存入到`act_hi_comment`中
		Authentication.setAuthenticatedUserId(applicant);

		// 先查询是否有给此工单特殊指定了审批人员列表，如果没有则查询对应工单类型通用的审批人员列表
		WorkOrderAssignee woAssignee = workOrderAssigneeMapper.getFirstAssigneeByOrderId(businessKey);
		if (woAssignee == null) {
			if (!ParameterUtils.isEmpty(assigneesOrderId)) {
				woAssignee = workOrderAssigneeMapper.getFirstAssigneeByOrderId(assigneesOrderId);
			}
			if (woAssignee == null) {
				String orderType = getOrderType(businessKey);
				woAssignee = workOrderAssigneeMapper.getFirstAssigneeByOrderId(orderType);
			}
		}

		putAssigneeInfoToVariables(woAssignee, variables);

		String processDefinitionKey = ProcessDefinitionKeyEnum.COMMON_EXAMINE_AND_APPROVE_FLOW
				.getProcessDefinitionKey();

		ProcessInstance processInstance;
		processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);

		String processInstanceId = processInstance.getProcessInstanceId();
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		String taskId = task.getId();

		taskService.addComment(taskId, processInstanceId, "提交申请");

		return processInstance;

	}
	
    /**
     * 通过orderId获取工单号前缀，然后查询出此工单是什么工单类型,比如QJ_20220501001,返回QJ
     * @param orderId 工单号
     */
    private String getOrderType(String orderId) {

        if (StringUtils.hasText(orderId)) {
        	String[] tempArray = orderId.split("_");
        	if(tempArray.length != 2){
        		throw new GzsendiException("请传入正确的工单号");
        	}
        	String orderIdPrex = tempArray[0];
        	//根据orderIdPrex前缀去数据库中查询
        	WorkFlowDef def = workFlowDefMapper.getByOrderIdPrex(orderIdPrex);
        	if(def == null){
        		 new GzsendiException("请传入正确的工单号");
        	}else {
        		return def.getOrderType();
        	}
        }
        throw new GzsendiException("请传入正确的工单号");
    }

	/**
	 * 审批申请
	 *
	 * @param businessKey
	 *            唯一业务key
	 * @param eaaResultEnum
	 *            审批结果枚举 result >= 0 ：通过，交给下一个人再审或没有下一个审批人时则归档结束流程 result ==
	 *            -999 ：不通过，直接取消审批(撤单) result < 0 && result != -999 ：不通过，下一步修改申请
	 * @param variables
	 *            流程实例流程变量，运行时会覆盖之前任务的值
	 * @param comment
	 *            备注信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void examAndApprove(String businessKey, TaskResultEnum eaaResultEnum, Map<String, Object> variables,
			String comment) {

		if (ParameterUtils.isEmpty(variables)) {
			variables = new HashMap<>();
		} else {
			variables = new HashMap<>(variables);
		}
		variables.put("businessKey", businessKey);
		
		if (TaskDefinitionKeyEnum.EXAM_AND_APPROVE != eaaResultEnum.getTaskDefinitionKeyEnum()) {
			throw new GzsendiException("请使用(" + TaskDefinitionKeyEnum.EXAM_AND_APPROVE.name() + ")对应的任务结果枚举");
		}
		// result > 0 ：通过，交给下一个人再审
		// result == 0 ：通过，流程结束，直接归档
		// result == -999 ：不通过，直接取消审批(撤单)
		// result < 0 && result != -999 ：不通过，下一步修改申请
		variables.put("result", eaaResultEnum.getResult());

		completeTask(businessKey, variables, comment, TaskDefinitionKeyEnum.EXAM_AND_APPROVE);
	}

	/**
	 * 审批不通过时，如果不是直接取消审批，则进行修改，然后再从初审开始重批
	 *
	 * @param businessKey
	 *            唯一业务key
	 * @param modifyResultEnum
	 *            修改结果枚举 result == -999 ：不通过，直接取消审批(撤单) result != -999
	 *            ：修改完成，下一步初审
	 * @param variables
	 *            流程实例流程变量，运行时会覆盖之前任务的值
	 * @param comment
	 *            备注信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void doModification(String businessKey, TaskResultEnum modifyResultEnum, Map<String, Object> variables,
			String comment) {
        if (ParameterUtils.isEmpty(variables)) {
            variables = new HashMap<>();
        } else {
            variables = new HashMap<>(variables);
        }
        if (TaskDefinitionKeyEnum.MODIFICATION != modifyResultEnum.getTaskDefinitionKeyEnum()) {
            throw new GzsendiException("请使用({})对应的任务结果枚举", TaskDefinitionKeyEnum.MODIFICATION.name());
        }

        //获取当前的任务的定义key，用于判断当前任务是待初审时的修改、初审不通过后的修改、初审通过待终审时的修改、终审不通过后的修改
        Task currentTask = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
        if (currentTask == null) {
            throw new GzsendiException("找不到工单({}), 无法进提交修改操作", businessKey);
        }
        String currentTaskDefinitionKey = currentTask.getTaskDefinitionKey();
        if (TaskDefinitionKeyEnum.EXAM_AND_APPROVE.getTaskDefinitionKey().equals(currentTaskDefinitionKey)) {
            if (isTheFirstAssigneeNow(businessKey, currentTask)) {
                //初审且未进行审批时，允许修改，但不用执行完成修改节点的操作，所以return
                return;
            } else {
                throw new GzsendiException("流程已经初审通过，不允许在待审批状态下修改工单");
            }
        }

        //result < 0 && result != -999 ：修改完成，下一步初审
        //result == -999 ：不通过，直接取消审批(撤单)
        variables.put("result", modifyResultEnum.getResult());

        completeTask(businessKey, variables, comment, TaskDefinitionKeyEnum.MODIFICATION);

	}

	/**
	 * 判断当前流程是否待第一个审批人进行处理
	 *
	 * @param businessKey
	 *            唯一业务key
	 * @param currentTask
	 *            当前任务节点，可传null，传null时将自动通过businessKey查询再使用
	 * @return boolean
	 */
	@Override
	public boolean isTheFirstAssigneeNow(String businessKey, Task currentTask) {
        if (ParameterUtils.isEmpty(businessKey)) {
            return false;
        }
        if (currentTask == null) {
            currentTask = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
            if (currentTask == null) {
                return false;
            }
        }
        Map<String, Object> processVariables = taskService.getVariables(currentTask.getId());
        return (Integer) processVariables.get("assigneeOrder") == 1;
	}

	@Override
	public Task getCurrentTaskByBusinessKey(String businessKey) {
		 return taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
	}

	@Override
	public String getBusinessKeyByTaskId(String taskId) {
        Task approveTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = approveTask.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return processInstance.getBusinessKey();
	}

	@Override
	public String getCurrentTaskDefinitionKey(String businessKey) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
        return task.getTaskDefinitionKey();
	}

	/**
	 * 逻辑删除流程实例
	 * @param businessKey
	 *            唯一业务key
	 * @param deleteReason
	 *            删除原因
	 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFlowLogically(String businessKey, String deleteReason) {
        //根据业务id查询出流程实例
        List<HistoricProcessInstance> historyProcess = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey).notDeleted().list();

        if (ParameterUtils.isEmpty(deleteReason)) {
            deleteReason = "未指明删除原因";
        }

        for (int i = 0; i < historyProcess.size(); i++) {
            HistoricProcessInstance historicProcessInstance = historyProcess.get(i);
            String processInstanceId = historicProcessInstance.getId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            //流程结束后，流程实例就查不到了，所以要避免删除不存在的实例而报错
            if (processInstance != null) {
                runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
            }
            //先删除runtime数据再删除历史数据(顺序不能换)
            historyService.deleteHistoricProcessInstance(processInstanceId);
        }
    }

	/**
	 * 物理删除流程实例
	 * @param businessKey
	 *            唯一业务key
	 */
	@Override
	public void deleteFlowPhysically(String businessKey) {
		flowableMapper.deleteProcessInstancePhysically(businessKey);
	}

	/**
	 * 进行一个完整的流程测试
	 * 
	 * @param businessKey
	 * @return
	 */
	@Override
	public int testCommonExamineAndApproveFlow(String businessKey) {
		
		if (ParameterUtils.isEmpty(businessKey)) {
			businessKey = "alibaba_libaba_baba_ba_a_" + new Random(1000).nextInt();
		}

		// =发起人发起流程开始 start=================================================================
		String applicant = "liujh"; // 申请人
		// 变量
		Map<String, Object> variables = new HashMap<String, Object>();
		// 1.启动流程
		startCommonFlow(businessKey, applicant, "", variables);
		
		// =发起人发起流程结束 endt=================================================================
		
		System.out.println();
		System.out.println();
		
		
		//2.   041717010624509564处理任务开始 start=================================================================
		// 2.041717010624509564查询任务
		// 查询出待办列表
		// 任务负责人
		String assignee = "041717010624509564";

		// 根据流程key 和 任务负责人 查询任务
		List<Task> list = taskService.createTaskQuery().processDefinitionKey("CommonExamineAndApproveFlow")
				.processInstanceBusinessKey(businessKey)
				.taskAssignee(assignee).list();

		//查询出任务并处理完成
		for (Task task : list) {
			System.out.println("流程实例id：" + task.getProcessInstanceId());
			System.out.println("任务id：" + task.getId());
			System.out.println("任务负责人：" + task.getAssignee());
			System.out.println("任务名称：" + task.getName());
			examAndApprove(businessKey, TaskResultEnum.EXAM_AND_APPROVE_PASS_TO_NEXT, null, "同意");

		}
		
		//查询出任务并处理完成
		List<Comment> list1 = listTaskComments(businessKey);
		for(Comment comment : list1) {
			System.out.println(comment.getUserId() +  comment.getTime() + comment.getFullMessage());
		}
		
		//2.   041717010624509564处理任务结束 end=================================================================
		
		
		System.out.println();
		System.out.println();
		
		
		// ==================================================================
		
		
		//3.   164657027540处理任务开始 start=================================================================
		//查询出待办列表
        //任务负责人
        assignee = "164657027540";

        //根据流程key 和 任务负责人 查询任务
        list = taskService.createTaskQuery()
                .processDefinitionKey("CommonExamineAndApproveFlow")
                .processInstanceBusinessKey(businessKey)
                .taskAssignee(assignee)
                .list();

        //查询出任务并处理完成
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
            examAndApprove(businessKey, TaskResultEnum.EXAM_AND_APPROVE_PASS_TO_END, null, "同意");

        }
        
        
        //打印审批历史
        list1 = listTaskComments(businessKey);
		for(Comment comment : list1) {
			System.out.println(comment.getUserId() +  comment.getTime() + comment.getFullMessage());
		}
		//3.   164657027540处理任务结束 end=================================================================
		
		
		System.out.println();
		System.out.println();
		
		
		//4.测试完成删除流程数据
		deleteFlowLogically(businessKey, "删除原因");
		
        // ==================================================================
        

		// examAndApprove(businessKey, -1, null, null);
		// doModification(businessKey, 1, null, null);
		// examAndApprove(businessKey, 1, null, null);
		// examAndApprove(businessKey, 1, null, null);
		// examAndApprove(businessKey, 1, null, null);
		// examAndApprove(businessKey, 0, null, null);

		// if (ParameterUtils.isEmpty(businessKey)) {
		// businessKey = "alibaba_libaba_baba_ba_a_" + new
		// Random(1000).nextInt();
		// }
		//
		// startCommonFlow(businessKey, null);
		//
		// examAndApprove(businessKey, -1, null, null);
		// doModification(businessKey, -999, null, null);

		// if (ParameterUtils.isEmpty(businessKey)) {
		// businessKey = "alibaba_libaba_baba_ba_a_" + new
		// Random(1000).nextInt();
		// }
		//
		// startCommonFlow(businessKey, null);
		//
		// examAndApprove(businessKey, -1, null, null);
		// doModification(businessKey, 1, null, null);
		// examAndApprove(businessKey, -999, null, null);

		// if (ParameterUtils.isEmpty(businessKey)) {
		// businessKey = "alibaba_libaba_baba_ba_a_" + new
		// Random(1000).nextInt();
		// }
		//
		// startCommonFlow(businessKey, null);
		//
		// examAndApprove(businessKey, -999, null, null);
		
		//flowableMapper.clearProcessInstancesPhysically();
		//deleteFlowLogically(businessKey, "resaon");
		//deleteFlowPhysically(businessKey);
		

		return 0;
	}

	/**
	 * 流程任务批注列表
	 *
	 * @param businessKey
	 *            唯一业务key
	 * @return 批注列表
	 */
	@Override
	public List<Comment> listTaskComments(String businessKey) {

		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey).singleResult();
        String processInstanceId = historicProcessInstance.getId();
        
        List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstanceId);
        if (ParameterUtils.isEmpty(processInstanceComments)) {
            return new ArrayList<>(0);
        }
        
		return processInstanceComments;
		
	}

	private void putAssigneeInfoToVariables(WorkOrderAssignee woAssignee, Map<String, Object> variables) {
		if (woAssignee == null) {
			throw new GzsendiException("找不到流程任务节点代理人列表的配置，请联系开发人员排查");
		}
		// 流程定义时使用assignee为代理人/审批人/处理人变量
		variables.put("assignee", woAssignee.getAssignee());
		// 指定当前工单的审批流程使用的是哪个版本的审批人员列表
		variables.put("assigneeVersion", woAssignee.getAssigneeVersion());
		// 指定当前工单的审批流程使用的是第几个代理人/审批人/处理人
		variables.put("assigneeOrder", woAssignee.getAssigneeOrder());
		// 设置工单状态，监听器会用于更新工单表的状态
		variables.put("orderStatus", woAssignee.getOrderStatus());
	}

	/**
	 * 完成任务
	 *
	 * @param businessKey
	 *            唯一业务key
	 * @param variables
	 *            完成任务时或给下一个节点使用的参数
	 * @param comment
	 *            完成任务时的备注/注释
	 * @param expectedTaskDefinitionKeyEnum
	 *            期望完成的任务定义key的枚举，用于判断被完成的任务是否期望的任务
	 */
	private void completeTask(String businessKey, Map<String, Object> variables, String comment,
			TaskDefinitionKeyEnum expectedTaskDefinitionKeyEnum) {

		if (ParameterUtils.isEmpty(businessKey)) {
			throw new GzsendiException("唯一业务key不能为空");
		}
		if (ParameterUtils.isEmpty(expectedTaskDefinitionKeyEnum)) {
			throw new GzsendiException("期望完成的任务定义key不能为空");
		}

		Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
		String taskDefinitionKey = task.getTaskDefinitionKey();
		String taskName = task.getName();

		String expectedTaskDefinitionKey = expectedTaskDefinitionKeyEnum.getTaskDefinitionKey();
		String expectedTaskDefinition = expectedTaskDefinitionKeyEnum.getTaskDefinition();
		if (!expectedTaskDefinitionKey.equals(taskDefinitionKey)) {
			throw new GzsendiException(String.format("当前流程执行到“%s”，非“%s”这一步", taskName, expectedTaskDefinition));
		}

		String taskId = task.getId();
		String assignee = task.getAssignee();
		String processInstanceId = task.getProcessInstanceId();
		Map<String, Object> processVariables = taskService.getVariables(taskId);
		String assigneesOrderId = (String) processVariables.get("assigneesOrderId");
		Integer assigneeVersion = (Integer) processVariables.get("assigneeVersion");
		Integer assigneeOrder = (Integer) processVariables.get("assigneeOrder");
		Integer result = (Integer) variables.get("result");
		Authentication.setAuthenticatedUserId(assignee);

		// 判断当前任务节点是否是“审批”
		if (TaskDefinitionKeyEnum.EXAM_AND_APPROVE.getTaskDefinitionKey().equals(taskDefinitionKey)) {
			if (result >= 0) {
				// 把下一个任务节点的代理人信息放入到variables中
				++assigneeOrder;
				putNextAssigneeInfoToVariables(assigneesOrderId, businessKey, assigneeVersion, assigneeOrder,
						variables);
				comment = ParameterUtils.isEmpty(comment) ? "通过" : comment;
			} else if (result == TaskResultEnum.EXAM_AND_APPROVE_REJECT_TO_CANCEL.getResult()) {
				variables.put("orderStatus", OrderStatusEnum.CANCELED.getValue());
			} else {
				variables.put("orderStatus", OrderStatusEnum.WAIT_FOR_UPDATE.getValue());
				comment = ParameterUtils.isEmpty(comment) ? "不通过" : comment;
			}
		}
		// 如果不是“审批”节点，那就是“修改”节点
		else {
			// -999代表取消审批，如果不是-999，则要从初审开始重新审批
			if (result == TaskResultEnum.MODIFICATION_REJECT_TO_CANCEL.getResult()) {
				variables.put("orderStatus", OrderStatusEnum.CANCELED.getValue());
			} else {
				// 把第一个任务节点的代理人信息放回到variables中
				assigneeOrder = 1;
				putNextAssigneeInfoToVariables(assigneesOrderId, businessKey, assigneeVersion, assigneeOrder,
						variables);
				comment = ParameterUtils.isEmpty(comment) ? "修改完成，重新审批" : comment;
			}
		}

		taskService.addComment(taskId, processInstanceId, ParameterUtils.isEmpty(comment) ? "（无批注）" : comment);
		taskService.complete(taskId, variables);
	}

	private void putNextAssigneeInfoToVariables(String assigneesOrderId, String businessKey, Integer assigneeVersion,
			Integer assigneeOrder, Map<String, Object> variables) {
		// 先查询是否有给此工单特殊指定了审批人员列表，如果没有则查询对应工单类型通用的审批人员列表
		WorkOrderAssignee woAssignee;
		woAssignee = workOrderAssigneeMapper.getByOrderIdVersionOrder(businessKey, assigneeVersion, assigneeOrder);
		if (woAssignee == null) {
			if (!ParameterUtils.isEmpty(assigneesOrderId)) {
				woAssignee = workOrderAssigneeMapper.getByOrderIdVersionOrder(assigneesOrderId, assigneeVersion,
						assigneeOrder);
			}
			if (woAssignee == null) {
				String orderType = getOrderType(businessKey);
				woAssignee = workOrderAssigneeMapper.getByOrderIdVersionOrder(orderType, assigneeVersion,
						assigneeOrder);
			}
		}

		// 如果查询不到下一个审批人了，那就说明可以归档结束流程了
		if (woAssignee == null) {
			variables.put("result", TaskResultEnum.EXAM_AND_APPROVE_PASS_TO_END.getResult());
			variables.put("orderStatus", OrderStatusEnum.FINISHED.getValue());
		} else {
			putAssigneeInfoToVariables(woAssignee, variables);
		}
	}

}
