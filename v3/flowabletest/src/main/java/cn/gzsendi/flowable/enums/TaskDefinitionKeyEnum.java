package cn.gzsendi.flowable.enums;

/**
 * 通用流程的任务节点定义Key的枚举
 *
 * @author Mr.XiHui
 * @date 2019/2/28
 */
public enum TaskDefinitionKeyEnum {

    /*** 一审/初审 */
    EXAM_AND_APPROVE("examAndApprove", "审批"),

    /*** 初审不通过后修改申请 */
    MODIFICATION("modification", "修改申请"),
    ;

    private final String taskDefinitionKey;

    private final String taskDefinition;

    /**
     * 通过taskDefinitionKey获取TaskDefinitionKeyEnum
     *
     * @param taskDefinitionKey 任务定义key
     * @return TaskDefinitionKeyEnum，如果没匹配到则返回null
     */
    public static TaskDefinitionKeyEnum getTaskDefinitionKeyEnum(String taskDefinitionKey) {

        TaskDefinitionKeyEnum[] taskDefinitionKeyEnums = TaskDefinitionKeyEnum.values();
        for (int i = 0; i < taskDefinitionKeyEnums.length; i++) {
            TaskDefinitionKeyEnum taskDefinitionKeyEnum = taskDefinitionKeyEnums[i];
            if (taskDefinitionKeyEnum.taskDefinitionKey.equals(taskDefinitionKey)) {
                return taskDefinitionKeyEnum;
            }
        }
        return null;
    }

    TaskDefinitionKeyEnum(String taskDefinitionKey, String taskDefinition) {
        this.taskDefinitionKey = taskDefinitionKey;
        this.taskDefinition = taskDefinition;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public String getTaskDefinition() {
        return taskDefinition;
    }

}
