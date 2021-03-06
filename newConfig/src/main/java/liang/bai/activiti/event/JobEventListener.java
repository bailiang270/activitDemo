package liang.bai.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description:   流程event监听器
 *
 * @author liang.bai@hand-china.com
 * @date 2018/10/10 11:54
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/10
 */
public class JobEventListener implements ActivitiEventListener {

    private static final Logger logger = LoggerFactory.getLogger(JobEventListener.class);

    @Override
    public void onEvent(ActivitiEvent event) {

        ActivitiEventType type = event.getType();
        String name = type.name();
        if (name.startsWith("TIMER")||name.startsWith("JOB")){
            logger.info("监听到job事件  {} \t {}",type,event.getProcessInstanceId());
        }


    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
