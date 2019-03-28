package liang.bai.activiti.interceptor;

import org.activiti.engine.debug.ExecutionTreeUtil;
import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.activiti.engine.logging.LogMDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description: mdc诊断   拦截器
 *
 * @author liang.bai@hand-china.com
 * @date 2018/10/9 19:04
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/9
 */
public class MDCcommandInvoker extends DebugCommandInvoker {

    private static final Logger logger = LoggerFactory.getLogger(MDCcommandInvoker.class);

    @Override
    public void executeOperation(Runnable runnable) {
        boolean mdcEnable = LogMDC.isMDCEnabled();
        LogMDC.setMDCEnabled(true);
        if (runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation) runnable;

            if (operation.getExecution() != null) {
                LogMDC.putMDCExecution(operation.getExecution());
            }

        }

        super.executeOperation(runnable);
        LogMDC.clear();
        if (!mdcEnable){
            LogMDC.setMDCEnabled(false);
        }
    }


}
