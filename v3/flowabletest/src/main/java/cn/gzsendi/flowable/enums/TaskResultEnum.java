package cn.gzsendi.flowable.enums;

import cn.gzsendi.framework.exception.GzsendiException;

/**
 * 通用流程的任务节点的处理结果的枚举
 *
 * @author Mr.XiHui
 * @date 2019/4/30
 */
public enum TaskResultEnum {

    /**
     * 审批节点的处理结果的含义
     * result > 0  ：通过，交给下一个人再审
     * result == 0 ：通过，流程结束，直接归档
     * result == -999 ：不通过，直接取消审批(撤单)
     * result < 0 && result != -999 ：不通过，下一步修改申请
     */
    EXAM_AND_APPROVE_PASS_TO_NEXT(1, "通过，交给下一个人再审", TaskDefinitionKeyEnum.EXAM_AND_APPROVE),
    EXAM_AND_APPROVE_PASS_TO_END(0, "通过，流程结束，直接归档", TaskDefinitionKeyEnum.EXAM_AND_APPROVE),
    EXAM_AND_APPROVE_REJECT_TO_MODIFICATION(-1, "不通过，下一步修改申请", TaskDefinitionKeyEnum.EXAM_AND_APPROVE),
    EXAM_AND_APPROVE_REJECT_TO_CANCEL(-999, "不通过，直接取消审批(撤单)", TaskDefinitionKeyEnum.EXAM_AND_APPROVE),

    /**
     * 修改节点的处理结果的含义
     * result == -999 ：不通过，直接取消审批(撤单)
     * result < 0 && result != -999 ：修改完成，下一步初审
     */
    MODIFICATION_PASS_TO_FIRST(1, "修改完成，下一步初审", TaskDefinitionKeyEnum.MODIFICATION),
    MODIFICATION_REJECT_TO_CANCEL(-999, "不通过，直接取消审批(撤单)", TaskDefinitionKeyEnum.MODIFICATION),
    ;

    private final int result;
    private final String resultDesc;
    private final TaskDefinitionKeyEnum taskDefinitionKeyEnum;

    TaskResultEnum(int result, String resultDesc, TaskDefinitionKeyEnum taskDefinitionKeyEnum) {
        this.result = result;
        this.resultDesc = resultDesc;
        this.taskDefinitionKeyEnum = taskDefinitionKeyEnum;
    }
    
    public static TaskResultEnum getTaskResultEnum(int value) {

    	TaskResultEnum[] taskResultEnums = TaskResultEnum.values();
        for (int i = 0; i < taskResultEnums.length; i++) {
        	TaskResultEnum taskResultEnum = taskResultEnums[i];
            if (taskResultEnum.result == value) {
                return taskResultEnum;
            }
        }
        throw new GzsendiException("请传入审批节点的处理结果result值");
    }

    public int getResult() {
        return result;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public TaskDefinitionKeyEnum getTaskDefinitionKeyEnum() {
        return taskDefinitionKeyEnum;
    }
}
