package cn.gzsendi.flowable.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.gzsendi.flowable.model.po.WorkFlowFormField;
import cn.gzsendi.flowable.service.WorkFlowFormFieldService;
import cn.gzsendi.framework.utils.JsonUtil;

@RestController
@RequestMapping("/workFlowFormField")
public class WorkFlowFormFieldController {
	
	@Autowired
	private WorkFlowFormFieldService workFlowFormFieldService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Object list(@RequestBody WorkFlowFormField dto) throws Exception {
		
		Assert.notNull(dto.getOrderType(), "orderType parameter is not allowed null.");
		
		List<WorkFlowFormField> list = workFlowFormFieldService.list(dto);
		
		return list;
	}
	
	@RequestMapping(value = "/queryById", method = RequestMethod.POST)
	public Object queryById(@RequestBody Map<String,Object> params) throws Exception {
		
		WorkFlowFormField workFlowFormField = workFlowFormFieldService.queryById(JsonUtil.getInteger(params, "id"));
		
		return workFlowFormField;
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public Object insert(@RequestBody WorkFlowFormField dto) throws Exception {
		
		workFlowFormFieldService.insert(dto);
		
		return true;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(@RequestBody WorkFlowFormField dto) throws Exception {
		
		Assert.notNull(dto.getId(), "id is not allowed null.");
		
		workFlowFormFieldService.update(dto);
		
		return true;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(@RequestBody Map<String,Object> params) throws Exception {
		
		List<Integer> idList = JsonUtil.getList(params, "ids", Integer.class);
		
		workFlowFormFieldService.delete(idList);
		
		return true;
	}
	
}

