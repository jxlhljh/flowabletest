package cn.gzsendi.flowable.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gzsendi.flowable.mapper.WorkFlowDefMapper;
import cn.gzsendi.flowable.model.po.WorkFlowDef;
import cn.gzsendi.flowable.service.WorkFlowDefService;

@Service
public class WorkFlowDefServiceImpl implements WorkFlowDefService {
	
	@Autowired
	private WorkFlowDefMapper workFlowMapper;

	@Override
	public List<WorkFlowDef> list(WorkFlowDef dto) {
		return workFlowMapper.list(dto);
	}

	@Override
	public WorkFlowDef getById(Integer id) {
		return workFlowMapper.getById(id);
	}

	@Override
	public int insert(WorkFlowDef workFlow) {
		return workFlowMapper.insert(workFlow);
	}

	@Override
	public int update(WorkFlowDef workFlow) {
		return workFlowMapper.update(workFlow);
	}

	@Override
	public int delete(List<Integer> ids) {
		return workFlowMapper.delete(ids);
	}

}
