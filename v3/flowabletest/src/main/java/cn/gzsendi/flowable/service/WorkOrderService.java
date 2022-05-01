package cn.gzsendi.flowable.service;

import java.util.List;

import cn.gzsendi.flowable.model.dto.MyDealWorkOrderDto;
import cn.gzsendi.flowable.model.dto.WorkOrderQueryDto;
import cn.gzsendi.flowable.model.po.WorkOrder;

/**
 * 工单服务
 *
 * @author Mr.XiHui
 * @date 2019/2/14
 */
public interface WorkOrderService {

    Integer deleteApplyOrder(String orderId);
    
	/**根据orderId查询*/
	public WorkOrder queryByOrderId(String orderId);
    
	/**新增工单并启动流程*/
	//approveUserVariables);页面传上来的审批人列表
	public WorkOrder addWorkOrder(String userId,String orderId,String orderType,int subjectId,String subjectType,String formJsonStr);
	
	/**修改工单并重新提交申请*/
	public void modificationWorkOrder(WorkOrder workOrder);
	
	/**列表查询，我处理的*/
	public List<MyDealWorkOrderDto> listMyDeal(WorkOrderQueryDto dto);

	/**listMyApply我申请的工单查询*/
	public List<WorkOrder> listMyApply(WorkOrderQueryDto dto);
	
	/**我已处理的的工单查询*/
	public List<WorkOrder> listMyExamAndApprove(WorkOrderQueryDto dto);	
}
