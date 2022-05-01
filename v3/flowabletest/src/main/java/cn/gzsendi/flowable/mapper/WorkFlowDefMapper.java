package cn.gzsendi.flowable.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.gzsendi.config.mybatis.UsingDefaultDB;
import cn.gzsendi.flowable.model.po.WorkFlowDef;

@UsingDefaultDB
public interface WorkFlowDefMapper {
	
	/**列表查询*/
	public List<WorkFlowDef> list(WorkFlowDef workFlow);
	
	/**根据主键查询*/
	public WorkFlowDef getById(Integer id);
	
	public WorkFlowDef getByOrderIdPrex(String orderIdPrex);
	
	public WorkFlowDef getByOrderType(String orderType);
	
	/**新增*/
	public int insert(WorkFlowDef workFlow);
	
	/**修改*/
	public int update(WorkFlowDef workFlow);
	
	/**删除*/
	public int delete(@Param("ids") List<Integer> ids);

}
