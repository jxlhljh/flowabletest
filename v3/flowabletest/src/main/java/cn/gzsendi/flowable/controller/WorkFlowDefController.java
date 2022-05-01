package cn.gzsendi.flowable.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.gzsendi.flowable.model.po.WorkFlowDef;
import cn.gzsendi.flowable.service.WorkFlowDefService;
import cn.gzsendi.framework.utils.JsonUtil;


/**
 * 通用的流程定义控制器（和flowable已无关，只是我们业务上定义的流程，底层直接用flowable配置的通用流程的那一套）
 * @author jxlhl
 */
@RestController
@RequestMapping("/workFlowDef")
public class WorkFlowDefController {
	
	@Autowired
	private WorkFlowDefService workFlowDefService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Object list() throws Exception {
		WorkFlowDef dto = new WorkFlowDef();
		List<WorkFlowDef> list = workFlowDefService.list(dto);
		return list;
	}
	
	/**根据主键查询*/
	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	public Object getById(@RequestBody Map<String,Object> params) throws Exception {
		/*这里没有加上字段校验，正式环境请加上*/
		return workFlowDefService.getById(JsonUtil.getInteger(params, "id"));
	}
	
	/**新增*/
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public Object insert(@RequestBody WorkFlowDef workFlow) throws Exception {
		/*这里没有加上字段校验，正式环境请加上*/
		workFlowDefService.insert(workFlow);
		return true;
	}
	
	/**修改*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(@RequestBody WorkFlowDef workFlow) throws Exception {
		/*这里没有加上字段校验，正式环境请加上*/
		workFlowDefService.update(workFlow);
		return true;
	}
	
	/**删除*/
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(@RequestBody Map<String,Object> params) throws Exception {
		/*这里没有加上字段校验，正式环境请加上*/
		List<Integer> idList = JsonUtil.getList(params, "ids", Integer.class);
		workFlowDefService.delete(idList);
		return true;
	}
	
}
