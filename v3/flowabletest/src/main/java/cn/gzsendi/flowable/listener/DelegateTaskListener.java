package cn.gzsendi.flowable.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import cn.gzsendi.flowable.enums.TaskDefinitionKeyEnum;

/**
 * 代理任务监听器
 *
 * @author Mr.XiHui
 * @date 2019/6/13
 */
public interface DelegateTaskListener {

    /**
     * 检验当前任务监听器实现类是否需要执行
     *
     * @param orderId           工单号
     * @param taskDefinitionKey 任务定义Key，参考{@link TaskDefinitionKeyEnum}
     * @param taskEventName     任务事件名，参考{@link TaskListener}中的常量
     * @param delegateTask      代理任务对象，orderId、taskDefinitionKey、taskEventName、variables都可以取自其中
     * @return boolean 校验默认false不通过，只有通过校验了才会执行这个接口的其他的方法
     * @see TaskDefinitionKeyEnum#EXAM_AND_APPROVE
     * @see TaskDefinitionKeyEnum#MODIFICATION
     * @see TaskListener#EVENTNAME_CREATE
     * @see TaskListener#EVENTNAME_COMPLETE
     * @see CommonFlowTaskListener#notify(DelegateTask)
     */
    default boolean validate(String orderId, String taskDefinitionKey, String taskEventName, DelegateTask delegateTask) {

        //校验默认不通过，只有通过校验了才会执行这个接口的其他的方法
        //项目启动时会统一初始化所有的DelegateTaskListener的实现类并声明成Bean，然后在CommonFlowTaskListener全部获取出来，
        //再通过此方法(validate)的当前流程所属工单(orderId)、当前的任务事件(taskDefinitionKey)、当前的任务事件(taskEventName)等条件
        //校验这个代理任务监听器是否对应当前流程所属工单，再选择执行这个接口的其他的方法
        return false;
    }

    /**
     * 执行（代理任务监听器）的通知逻辑，这里可以做一些如发送邮件，写其他相关业务逻辑表等的操作
     *
     * @param delegateTask 代理任务对象，orderId、taskDefinitionKey、taskEventName、variables都可以取自其中
     */
    void doTaskNotify(DelegateTask delegateTask);
}
