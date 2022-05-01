package cn.gzsendi.flowable.mapper;

import org.apache.ibatis.annotations.Delete;

import cn.gzsendi.config.mybatis.UsingDefaultDB;

/**
 * flowable的数据操作接口
 * @author liujinghua
 * @date 2022/4/30
 */
@UsingDefaultDB
public interface FlowableMapper {

    /**
     * 物理删除流程实例，包含变量数据和历史数据
     * @param businessKey 唯一业务key
     * @return int
     */
    @Delete("DELETE hp,el,ht,hv,hi,ha,rv,rt,ri,re,hc FROM `act_hi_procinst` hp " +
            "LEFT JOIN `act_evt_log` el ON el.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_hi_taskinst` ht ON ht.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_hi_varinst` hv ON hv.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_hi_identitylink` hi ON hi.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_hi_actinst` ha ON ha.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_hi_comment` hc ON hc.TASK_ID_ = ht.ID_ " +
            "LEFT JOIN `act_ru_variable` rv ON rv.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_ru_task` rt ON rt.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_ru_identitylink` ri ON ri.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_ru_execution` re ON re.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "WHERE hp.BUSINESS_KEY_ = #{businessKey}")
    int deleteProcessInstancePhysically(String businessKey);

    /**
     * 清除与work_order表无对应的流程实例，包含变量数据和历史数据
     *
     * @return int
     */
    @Delete("DELETE hp,el,ht,hv,hi,ha,rv,rt,ri,re,hc FROM `act_hi_procinst` hp " +
            "LEFT JOIN `act_evt_log` el ON el.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_hi_taskinst` ht ON ht.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_hi_varinst` hv ON hv.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_hi_identitylink` hi ON hi.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_hi_actinst` ha ON ha.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_hi_comment` hc ON hc.TASK_ID_ = ht.ID_ " +
            "LEFT JOIN `act_ru_variable` rv ON rv.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_ru_task` rt ON rt.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_ru_identitylink` ri ON ri.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "LEFT JOIN `act_ru_execution` re ON re.PROC_INST_ID_ = hp.PROC_INST_ID_ " +
            "WHERE hp.BUSINESS_KEY_ NOT IN (SELECT `order_id` FROM `work_order`);")
    int clearProcessInstancesPhysically();
}