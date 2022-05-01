package cn.gzsendi.flowable.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gzsendi.flowable.mapper.WorkFlowFormFieldMapper;
import cn.gzsendi.flowable.model.po.WorkFlowFormField;
import cn.gzsendi.flowable.service.WorkFlowFormFieldService;


@Service
public class WorkFlowFormFieldServiceImpl implements WorkFlowFormFieldService{
	
	@Autowired
	private WorkFlowFormFieldMapper workFlowFormFieldMapper;

	public List<WorkFlowFormField> list(WorkFlowFormField dto) {
        List<WorkFlowFormField> list = workFlowFormFieldMapper.list(dto);
		return list;
	}

	public WorkFlowFormField queryById(Integer id) {
		return workFlowFormFieldMapper.queryById(id);
	}

	public int insert(WorkFlowFormField workFlowFormField) {
		return workFlowFormFieldMapper.insert(workFlowFormField);
	}

	public int update(WorkFlowFormField workFlowFormField) {
		return workFlowFormFieldMapper.update(workFlowFormField);
	}

	public int delete(List<Integer> ids) {
		return workFlowFormFieldMapper.delete(ids);
	}

}



