package cn.gzsendi.flowable.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gzsendi.flowable.enums.OrderStatusEnum;
import cn.gzsendi.flowable.enums.TaskResultEnum;
import cn.gzsendi.flowable.mapper.WorkFlowDefMapper;
import cn.gzsendi.flowable.mapper.WorkOrderMapper;
import cn.gzsendi.flowable.model.dto.MyDealWorkOrderDto;
import cn.gzsendi.flowable.model.dto.WorkOrderQueryDto;
import cn.gzsendi.flowable.model.po.WorkFlowDef;
import cn.gzsendi.flowable.model.po.WorkOrder;
import cn.gzsendi.flowable.service.CommonFlowService;
import cn.gzsendi.flowable.service.WorkOrderService;
import cn.gzsendi.framework.exception.GzsendiException;

@Service
public class WorkOrderServiceImpl implements WorkOrderService{
	
	@Autowired
	private WorkOrderMapper workOrderMapper;
	
	@Autowired
	private CommonFlowService commonFlowService;
	
	@Autowired
	private WorkFlowDefMapper workFlowDefMapper;

	@Override
	public Integer deleteApplyOrder(String orderId) {
		return 0;
	}

	/**
	 * approveUserVariables:页面传上来的审批人列表，用来替换fromForm的审批人结点中的handler
	 */
	@Transactional
	public WorkOrder addWorkOrder(String userId,String orderId,String orderType,int subjectId,String subjectType,String formJsonStr){
		
		//查一遍orderType,校验一下是否合法的orderType
		WorkFlowDef def = workFlowDefMapper.getByOrderType(orderType);
		if(def == null){
			throw new GzsendiException("工单类型orderType参数非法");
		}
		
		/*****提交申请,测试，正式环境根据情况调整**********************************************************/
		WorkOrder workOrder = new WorkOrder();
		workOrder.setOrderId(orderId);//orderId,工单ID
		workOrder.setOrderType(orderType);//工单类型
		workOrder.setSubjectId(subjectId);//工单所属主体的ID.关联其他表时，存其他表的记录的ID
		workOrder.setSubjectType(subjectType);//工单所属主体的类型，关联其他表时，存其他表的类型
		workOrder.setApplicant(userId);//申请人登陆账号
		workOrder.setApplicationTime(new Date());//申请时间
		workOrder.setOrderSummary("{\"title\":\""+def.getOrderName()+"\",\"createTime\":\""+new Date()+"\",\"userId\":\""+userId+"\"}");
		workOrder.setOrderStatus(OrderStatusEnum.WAIT_FOR_VERIFY.getValue());//工单状态\
		workOrder.setReason("");//提单原因
		workOrder.setFormData(formJsonStr);//工单可变字段数据,json文本存储
		workOrder.setRemark("");//remark
		workOrder.setHandler("");//处理人
		workOrder.setCreateUser(userId);
		workOrder.setCreateTime(new Date());
		workOrder.setUpdateUser(userId);
		workOrder.setUpdateTime(new Date());
		
		//保存工单表单
		workOrderMapper.insert(workOrder);
		
		//1.启动流程
		commonFlowService.startCommonFlow(orderId, workOrder.getApplicant(), "", null);
		
		return workOrder;
		
	}
	
	/**修改工单并重新提交申请*/
	@Transactional
	public void modificationWorkOrder(WorkOrder workOrder){
		
		WorkOrder orderToUpdate = workOrderMapper.queryByOrderId(workOrder.getOrderId());
		if(orderToUpdate == null){
			throw new GzsendiException("未找到工单, 工单号：" +workOrder.getOrderId());
		}
		
		orderToUpdate.setFormData(workOrder.getFormData());
		orderToUpdate.setOrderSummary(workOrder.getOrderSummary());
		orderToUpdate.setUpdateUser(workOrder.getHandler());
		orderToUpdate.setUpdateTime(new Date());
		workOrderMapper.updateWorkOrder(orderToUpdate);
		
		//调用service进行审批操作
		String businessKey = workOrder.getOrderId();
		commonFlowService.doModification(businessKey, TaskResultEnum.MODIFICATION_PASS_TO_FIRST, null, "重新提交申请");
		
	}
	
	/**根据orderId查询*/
	public WorkOrder queryByOrderId(String orderId){
		return workOrderMapper.queryByOrderId(orderId);
	}
	
	/**列表查询，我处理的*/
	public List<MyDealWorkOrderDto> listMyDeal(WorkOrderQueryDto dto){
		
        List<MyDealWorkOrderDto> list = workOrderMapper.listMyDeal(dto.getUserId());
        
		return list;
		
	}
	
	/**listMyApply我申请的工单查询*/
	public List<WorkOrder> listMyApply(WorkOrderQueryDto dto){
        List<WorkOrder> list = workOrderMapper.listMyApply(dto.getUserId());
		return list;
	}
	
	/**我已处理的的工单查询*/
	public List<WorkOrder> listMyExamAndApprove(WorkOrderQueryDto dto){
        List<WorkOrder> list = workOrderMapper.listMyExamAndApprove(dto.getUserId());
		return list;
	}

}
