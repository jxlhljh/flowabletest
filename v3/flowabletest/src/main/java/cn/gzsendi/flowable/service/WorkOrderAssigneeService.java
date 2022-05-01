package cn.gzsendi.flowable.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.gzsendi.flowable.model.po.WorkOrderAssignee;

public interface WorkOrderAssigneeService {
	
	List<WorkOrderAssignee> selectSelective(WorkOrderAssignee workOrderAssignee);
	
	WorkOrderAssignee selectByPrimaryKey(String orderId, Integer assigneeVersion, String assignee);
	
	int insert(WorkOrderAssignee workOrderAssignee);
	
	int updateByPrimaryKey(WorkOrderAssignee workOrderAssignee);
	
    int deleteByPrimaryKey(@Param("orderId") String orderId,
            @Param("assigneeVersion") Integer assigneeVersion,
            @Param("assignee") String assignee);

}
