package cn.gzsendi.flowable.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.flowable.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.gzsendi.flowable.enums.TaskResultEnum;
import cn.gzsendi.flowable.mapper.WorkFlowDefMapper;
import cn.gzsendi.flowable.model.dto.MyDealWorkOrderDto;
import cn.gzsendi.flowable.model.dto.WorkOrderApproveDto;
import cn.gzsendi.flowable.model.dto.WorkOrderQueryDto;
import cn.gzsendi.flowable.model.po.WorkFlowDef;
import cn.gzsendi.flowable.model.po.WorkOrder;
import cn.gzsendi.flowable.service.CommonFlowService;
import cn.gzsendi.flowable.service.WorkOrderService;
import cn.gzsendi.framework.exception.GzsendiException;
import cn.gzsendi.framework.utils.JsonUtil;

@RestController
@RequestMapping("/workOrder")
public class WorkOrderController {
	
	@Autowired
	private WorkOrderService workOrderService;
	
	@Autowired
	private CommonFlowService commonFlowService;
	
	@Autowired
	private WorkFlowDefMapper workFlowDefMapper;
	
	//待我处理的工单列表
	@RequestMapping(value = "/listMyDeal", method = RequestMethod.POST)
	public Object listMyDeal(@RequestBody WorkOrderQueryDto dto) throws Exception {

		//正式环境改成读取当前用户，这里采用从request.parameter中取当前用户
		String userId = dto.getUserId();
		Assert.notNull(userId,"userId Parameter is null.");
		
		//列出待我处理的工单
		List<MyDealWorkOrderDto> list = workOrderService.listMyDeal(dto);
		
		return list;
	}
	
	//我申请的的工单列表
	@RequestMapping(value = "/listMyApply", method = RequestMethod.POST)
	public Object listMyApply(@RequestBody WorkOrderQueryDto dto) throws Exception {
		
		//正式环境改成读取当前用户，这里采用从request.parameter中取当前用户
		String userId = dto.getUserId();
		Assert.notNull(userId,"userId Parameter is null.");
		
		//列出待我处理的工单
		List<WorkOrder> list = workOrderService.listMyApply(dto);
		
		return list;
	}
	
	//我已处理的的工单列表
	@RequestMapping(value = "/listMyExamAndApprove", method = RequestMethod.POST)
	public Object listMyExamAndApprove(@RequestBody WorkOrderQueryDto dto) throws Exception {
		
		//正式环境改成读取当前用户，这里采用从request.parameter中取当前用户
		String userId = dto.getUserId();
		Assert.notNull(userId,"userId Parameter is null.");
		
		//列出已处理的的工单列表
		List<WorkOrder> list = workOrderService.listMyExamAndApprove(dto);
		
		return list;
	}
	
	//@ApiOperation(value = "增加工单测试", httpMethod = HttpMethodContstans.POST)
	@RequestMapping(value = "/addWorkOrder", method = RequestMethod.POST)
	public Object addWorkOrder(@RequestBody Map<String,Object> params) throws Exception {
		
		String jsonStr = URLDecoder.decode(JsonUtil.toJSONString(params),StandardCharsets.UTF_8.name());
		Map<String,Object> dataParam = JsonUtil.castToObject(jsonStr);
		String orderType = JsonUtil.getString(dataParam, "orderType");//工单类型
		String userId = JsonUtil.getString(dataParam, "userId");//用户
		String formJsonStr = JsonUtil.toJSONString(dataParam.get("formJson"));//动态表单数据
		
		Assert.notNull(orderType, "orderType is null.");
		Assert.notNull(userId, "userId is null.");
		Assert.notNull(formJsonStr, "formJsonStr is null.");
		
		WorkFlowDef def = workFlowDefMapper.getByOrderType(orderType);
		if(def == null){
			throw new GzsendiException("工单类型参数非法.");
		}
		String orderId = def.getOrderIdPrex() + "_" + System.currentTimeMillis();//用毫秒数模拟，正式环境改成工单ID生成服务调用，idGeneratorService.generateId(IdTypeEnum.USER_ID);
		
		//新加表单并启动流程
		WorkOrder workOrder = workOrderService.addWorkOrder(userId, orderId, orderType, 1, "1", formJsonStr);
		return workOrder;
	}
	
	//工单审核
	//examAndApprove
	@RequestMapping(value = "/examAndApprove", method = RequestMethod.POST)
	public Object examAndApprove(@RequestBody WorkOrderApproveDto dto) throws Exception {
	
		String userId = dto.getUserId();
		String orderId = dto.getOrderId();
		String agree = dto.getAgree();
		String comment = dto.getComment() == null ? "" : dto.getComment();
		Assert.notNull(dto,"userId Parameter is null.");
		Assert.notNull(dto,"orderId Parameter is null.");
		Assert.notNull(dto,"agree Parameter is null.");
		
		//调用service进行审批操作
		String businessKey = orderId;
		commonFlowService.examAndApprove(businessKey, TaskResultEnum.getTaskResultEnum(Integer.parseInt(agree)), null, comment);
		
		return true;
		
	}
	
	//工单被打回后修改并重新提交给审批人审批
	//@ApiOperation(value = "工单被打回后修改并重新提交给审批人审批", httpMethod = HttpMethodContstans.POST)
	@RequestMapping(value = "/modificationWorkOrder", method = RequestMethod.POST)
	public Object modificationWorkOrder(@RequestBody Map<String,Object> params) throws Exception {
		
		String jsonStr = URLDecoder.decode(JsonUtil.toJSONString(params),StandardCharsets.UTF_8.name());
		Map<String,Object> dataParam = JsonUtil.castToObject(jsonStr);
		String orderId = JsonUtil.getString(dataParam, "orderId");//工单编号
		String formJsonStr = JsonUtil.toJSONString(dataParam.get("formJson"));//动态表单数据
		Assert.notNull(orderId,"orderId Parameter is null.");
		Assert.notNull(formJsonStr,"formData Parameter is null.");
		WorkOrder workOrder = new WorkOrder();
		workOrder.setOrderId(orderId);
		workOrder.setFormData(formJsonStr);
		workOrderService.modificationWorkOrder(workOrder);
		
		return true;
		
	}
	
	//根据orderId查询", httpMethod = HttpMethodContstans.POST)
	@RequestMapping(value = "/queryByOrderId", method = RequestMethod.POST)
	public Object queryByOrderId(@RequestBody WorkOrderQueryDto dto) throws Exception {
		
		String orderId = dto.getOrderId();
		Assert.notNull(orderId,"orderId Parameter is null.");
		WorkOrder workOrder = workOrderService.queryByOrderId(orderId);
		
		return workOrder;
	}
	
	//审批历史
	@RequestMapping(value = "/listTaskComments", method = RequestMethod.POST)
	public Object listTaskComments(@RequestBody WorkOrderQueryDto dto) throws Exception {
		
		String orderId = dto.getOrderId();
		Assert.notNull(orderId,"orderId Parameter is null.");
		List<Comment> list = commonFlowService.listTaskComments(orderId);
		return list;
		
	}
	
	
/*	//工单审核
	//examAndApprove
	@RequestMapping(value = "/examAndApprove", method = RequestMethod.POST)
	public Object examAndApprove(@RequestBody RequestParams<WorkOrderApproveDto> params) throws Exception {
	
		WorkOrderApproveDto dto = params.getData();
		Assert.notNull(dto,"data Parameter is null.");
		
		String userId = dto.getUserId();
		String orderId = dto.getOrderId();
		int flowNodeId = dto.getFlowNodeId();
		if(flowNodeId == 0) {
			throw new GzsendiException("flowNodeId Parameter is null.");
		}
		String agree = dto.getAgree();
		String comment = dto.getComment() == null ? "" : dto.getComment();
		Assert.notNull(dto,"userId Parameter is null.");
		Assert.notNull(dto,"orderId Parameter is null.");
		Assert.notNull(dto,"agree Parameter is null.");
		
		//调用service进行审批操作
		workFlowService.examAndApprove(userId, userId, orderId, flowNodeId, agree, comment);
		
		return true;
		
	}*/
	
}

