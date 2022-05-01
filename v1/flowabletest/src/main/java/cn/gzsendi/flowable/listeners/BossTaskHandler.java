package cn.gzsendi.flowable.listeners;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class BossTaskHandler implements TaskListener {
	private static final long serialVersionUID = 1L;

	@Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("老板");
    }
 
}