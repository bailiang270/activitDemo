package config;

import com.google.common.collect.Maps;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * description:
 *
 * @author liang.bai@hand-china.com
 * @date 2018/10/9 19:24
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/9
 */
public class ConfigHistoryLevelTest {


    private static final Logger logger = LoggerFactory.getLogger(ConfigHistoryLevelTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_history.cfg.xml");

    @Test
    @Deployment(resources = {"my-process_mdcrror.bpmn20.xml"})
    public void test() {

        //启动流程
        Map<String,Object> params = Maps.newHashMap();
        params.put("keyStart1","value1");
        params.put("keyStart2","value2");
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process",params);
        //修改变量
        List<Execution> executions = activitiRule.getRuntimeService().createExecutionQuery().listPage(0, 100);
        for (Execution exception:executions){
            logger.info("executions = {}" , exception);
        }
        logger.info("execution.size",executions.size());
        String id = executions.iterator().next().getId();
        activitiRule.getRuntimeService().setVariable(id,"keyStart1","value1____");

        //提交表单
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Map<String, String> properties = Maps.newHashMap();
        properties.put("formKey1","formValue1");
        properties.put("formKey2","formValue2");
        activitiRule.getFormService().submitTaskFormData(task.getId(),properties);


        //输出历史活动
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance:historicActivityInstances){
            logger.info("HistoricActivityInstance = {}",historicActivityInstance);
        }
        logger.info("HistoricActivityInstance.size = {}",historicActivityInstances.size());

        //输出历史表单
        List<HistoricTaskInstance> historicTaskInstances = activitiRule.getHistoryService()
                .createHistoricTaskInstanceQuery().listPage(0, 100);
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances){
            logger.info("HistoricTaskInstance = {}" , historicTaskInstance);
        }
        logger.info("HistoricTaskInstance.size = {}",historicTaskInstances.size());

        //输出历史详情
        List<HistoricDetail> historicDetails = activitiRule.getHistoryService()
                .createHistoricDetailQuery().formProperties().listPage(0, 100);
        for (HistoricDetail historicDetail:historicDetails){
            logger.info("HistoricDetail = {}",historicDetail);
        }
        logger.info("HistoricDetail.size = {}",historicDetails.size());

        List<HistoricDetail> details = activitiRule.getHistoryService()
                .createHistoricDetailQuery().listPage(0, 100);
        for (HistoricDetail detail :details){
            logger.info("detail = {}" , detail);
        }
        logger.info("detail.size = {}",details.size());


    }

}
