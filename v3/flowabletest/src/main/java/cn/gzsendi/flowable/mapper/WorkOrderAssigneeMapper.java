package cn.gzsendi.flowable.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.gzsendi.config.mybatis.UsingDefaultDB;
import cn.gzsendi.flowable.model.dto.WorkOrderAssigneeDTO;
import cn.gzsendi.flowable.model.po.WorkOrderAssignee;

@UsingDefaultDB
public interface WorkOrderAssigneeMapper {

    int insert(WorkOrderAssignee workOrderAssignee);

    int deleteByPrimaryKey(@Param("orderId") String orderId,
                           @Param("assigneeVersion") Integer assigneeVersion,
                           @Param("assignee") String assignee);

    int updateByPrimaryKey(WorkOrderAssignee workOrderAssignee);

    WorkOrderAssignee selectByPrimaryKey(@Param("orderId") String orderId,
                                         @Param("assigneeVersion") Integer assigneeVersion,
                                         @Param("assignee") String assignee);

    List<WorkOrderAssignee> listByOrderIdAndVersion(@Param("orderId") String orderId,
                                                    @Param("assigneeVersion") Integer assigneeVersion);

    List<WorkOrderAssignee> selectAll();

    List<WorkOrderAssignee> selectSelective(WorkOrderAssignee workOrderAssignee);

    List<WorkOrderAssignee> selectLatestVersionByOrderId(@Param("orderId") String orderId);

    List<WorkOrderAssigneeDTO> selectLatestVersionDTOByOrderId(@Param("orderId") String orderId);

    WorkOrderAssignee getFirstAssigneeByOrderId(@Param("orderId") String orderId);

    WorkOrderAssignee getByOrderIdVersionOrder(@Param("orderId") String orderId,
                                               @Param("assigneeVersion") Integer assigneeVersion,
                                               @Param("assigneeOrder") Integer assigneeOrder);

    /**
     * 判断流程是否存在（有些业务不是必须流程的，所以用它来判断业务是否要走流程）
     *
     * @param orderId 获取任务节点列表(代理人列表)的工单号
     * @return boolean
     */
    @Select("select count(*) from wo_assignee where order_id = #{orderId}")
    boolean doesTheAssigneesExist(String orderId);
}