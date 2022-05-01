package cn.gzsendi.flowable.service;

import java.util.List;

import cn.gzsendi.flowable.model.po.WorkFlowFormField;

public interface WorkFlowFormFieldService {

	/**列表查询*/
	public List<WorkFlowFormField> list(WorkFlowFormField dto);
	
	/**根据主键查询*/
	public WorkFlowFormField queryById(Integer id);
	
	/**新增*/
	public int insert(WorkFlowFormField workFlowFormField);
	
	/**修改*/
	public int update(WorkFlowFormField workFlowFormField);
	
	/**删除*/
	public int delete(List<Integer> ids);
	
}
