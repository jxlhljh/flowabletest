package cn.gzsendi.flowable.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * 代理执行监听器
 *
 * @author Mr.XiHui
 * @date 2019/6/13
 */
public interface DelegateExecListener {

    /**
     * 检验当前执行监听器实现类是否需要执行
     *
     * @param orderId           工单号
     * @param eventName         事件名，参考{@link ExecutionListener}中的常量
     * @param delegateExecution 代理执行对象，orderId、eventName、processInstanceId、variables都可以取自其中
     * @return boolean 校验默认false不通过，只有通过校验了才会执行这个接口的其他的方法
     * @see ExecutionListener#EVENTNAME_START
     * @see ExecutionListener#EVENTNAME_END
     * @see CommonFlowExecListener#notify(org.activiti.engine.delegate.DelegateExecution)
     */
    default boolean validate(String orderId, String eventName, DelegateExecution delegateExecution) {

        //校验默认不通过，只有通过校验了才会执行这个接口的其他的方法
        //项目启动时会统一初始化所有的DelegateExecListener的实现类并声明成Bean，然后在CommonFlowExecListener全部获取出来，
        //再通过此方法(validate)的当前流程所属工单(orderId)、当前的执行事件(eventName)等条件
        //校验这个代理执行监听器是否对应当前流程所属工单，再选择执行这个接口的其他的方法
        return false;
    }

    /**
     * 执行（代理执行监听器）的通知逻辑，这里可以做一些如发送邮件，写其他相关业务逻辑表等的操作
     *
     * @param delegateExecution 代理执行对象，orderId、eventName、processInstanceId、variables都可以取自其中
     */
    void doExecNotify(DelegateExecution delegateExecution);
}
