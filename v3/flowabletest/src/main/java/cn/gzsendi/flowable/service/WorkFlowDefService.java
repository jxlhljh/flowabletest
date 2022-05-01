package cn.gzsendi.flowable.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.gzsendi.flowable.model.po.WorkFlowDef;

/**
 * 此类只是维护用户定义的流程名称，操作的是work_flow表和flowable引擎无关
 * @author jxlhl
 *
 */
public interface WorkFlowDefService {
	
	/**列表查询*/
	public List<WorkFlowDef> list(WorkFlowDef dto);
	
	/**根据主键查询*/
	public WorkFlowDef getById(Integer id);
	
	/**新增*/
	public int insert(WorkFlowDef workFlow);
	
	/**修改*/
	public int update(WorkFlowDef workFlow);
	
	/**删除*/
	public int delete(@Param("ids") List<Integer> ids);
	
	
    
}
