package cn.gzsendi.flowable.enums;

/**
 * 流程定义key枚举
 *
 * @author Mr.XiHui
 * @date 2019/4/30
 */
public enum ProcessDefinitionKeyEnum {

    /**
     * 通用流程的流程定义key
     */
    COMMON_EXAMINE_AND_APPROVE_FLOW("CommonExamineAndApproveFlow"),
    ;

    private final String processDefinitionKey;

    ProcessDefinitionKeyEnum(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }
}
