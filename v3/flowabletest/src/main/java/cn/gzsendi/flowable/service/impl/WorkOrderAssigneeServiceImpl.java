package cn.gzsendi.flowable.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gzsendi.flowable.mapper.WorkOrderAssigneeMapper;
import cn.gzsendi.flowable.model.po.WorkOrderAssignee;
import cn.gzsendi.flowable.service.WorkOrderAssigneeService;

@Service
public class WorkOrderAssigneeServiceImpl implements WorkOrderAssigneeService{
	
	@Autowired
	private WorkOrderAssigneeMapper workOrderAssigneeMapper;

	@Override
	public List<WorkOrderAssignee> selectSelective(WorkOrderAssignee workOrderAssignee) {
		return workOrderAssigneeMapper.selectSelective(workOrderAssignee);
	}

	@Override
	public WorkOrderAssignee selectByPrimaryKey(String orderId, Integer assigneeVersion, String assignee) {
		return workOrderAssigneeMapper.selectByPrimaryKey(orderId, assigneeVersion, assignee);
	}

	@Override
	public int insert(WorkOrderAssignee workOrderAssignee) {
		return workOrderAssigneeMapper.insert(workOrderAssignee);
	}

	@Override
	public int updateByPrimaryKey(WorkOrderAssignee workOrderAssignee) {
		return workOrderAssigneeMapper.updateByPrimaryKey(workOrderAssignee);
	}

	@Override
	public int deleteByPrimaryKey(String orderId, Integer assigneeVersion, String assignee) {
		return workOrderAssigneeMapper.deleteByPrimaryKey(orderId, assigneeVersion, assignee);
	}

}
