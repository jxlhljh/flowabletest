package cn.gzsendi.flowable.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.gzsendi.flowable.model.po.WorkOrderAssignee;
import cn.gzsendi.flowable.service.WorkOrderAssigneeService;

@RestController
@RequestMapping("/workOrderAssignee")
public class WorkOrderAssigneeController {
	
	@Autowired
	private WorkOrderAssigneeService workOrderAssigneeService;
	
	@RequestMapping(value = "/selectSelective", method = RequestMethod.POST)
	public Object selectSelective(@RequestBody WorkOrderAssignee dto) throws Exception {
		
		Assert.notNull(dto.getOrderId(), "orderId parameter is not allowed null.");
		List<WorkOrderAssignee> list = workOrderAssigneeService.selectSelective(dto);
		return list;
	}

	@RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.POST)
	public Object selectByPrimaryKey(@RequestBody WorkOrderAssignee dto) throws Exception {
		
		Assert.notNull(dto.getOrderId(), "orderId parameter is not allowed null.");
		Assert.notNull(dto.getAssigneeVersion(), "assigneeVersion parameter is not allowed null.");
		Assert.notNull(dto.getAssignee(), "assignee parameter is not allowed null.");
		
		WorkOrderAssignee  workOrderAssignee = workOrderAssigneeService.selectByPrimaryKey(dto.getOrderId(),dto.getAssigneeVersion(),dto.getAssignee());
		return workOrderAssignee;
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public Object insert(@RequestBody WorkOrderAssignee dto) throws Exception {
		
		Assert.notNull(dto.getOrderId(), "orderId parameter is not allowed null.");
		Assert.notNull(dto.getAssigneeVersion(), "assigneeVersion parameter is not allowed null.");
		Assert.notNull(dto.getAssignee(), "assignee parameter is not allowed null.");
		
		dto.setUpdateUser("0");//写死成0，正式上线后改成正确的
		dto.setCreateUser("0");
		dto.setCreateTime(new Date());
		dto.setUpdateTime(new Date());
		
		workOrderAssigneeService.insert(dto);
		
		return true;
	}
	
	@RequestMapping(value = "/updateByPrimaryKey", method = RequestMethod.POST)
	public Object updateByPrimaryKey(@RequestBody WorkOrderAssignee dto) throws Exception {
		
		Assert.notNull(dto.getOrderId(), "orderId parameter is not allowed null.");
		Assert.notNull(dto.getAssigneeVersion(), "assigneeVersion parameter is not allowed null.");
		Assert.notNull(dto.getAssignee(), "assignee parameter is not allowed null.");
		
		dto.setUpdateUser("0");//写死成0，正式上线后改成正确的
		dto.setUpdateTime(new Date());
		
		workOrderAssigneeService.updateByPrimaryKey(dto);
		
		return true;
	}	
	
	@RequestMapping(value = "/deleteByPrimaryKey", method = RequestMethod.POST)
	public Object deleteByPrimaryKey(@RequestBody WorkOrderAssignee dto) throws Exception {
		
		Assert.notNull(dto.getOrderId(), "orderId parameter is not allowed null.");
		Assert.notNull(dto.getAssigneeVersion(), "assigneeVersion parameter is not allowed null.");
		Assert.notNull(dto.getAssignee(), "assignee parameter is not allowed null.");
		workOrderAssigneeService.deleteByPrimaryKey(dto.getOrderId(), dto.getAssigneeVersion(), dto.getAssignee());
		return true;
	}

    
    
    
}
