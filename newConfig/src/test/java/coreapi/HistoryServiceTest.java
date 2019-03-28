package coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author liang.bai@hand-china.com
 * @date 2018/10/15 18:40
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/15
 */
public class HistoryServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(HistoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testHistory(){

        HistoryService historyService = activitiRule.getHistoryService();

        ProcessInstanceBuilder processInstanceBuilder = activitiRule.getRuntimeService().createProcessInstanceBuilder();
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("key1","value1");
        variables.put("key2","value2");
        variables.put("key3","value3");
        Map<String,Object> transientVariables = Maps.newHashMap();

        historyService.createHistoricTaskInstanceQuery().taskAssignee("12321");
        transientVariables.put("tkey1","tvalue1");
        //启动流程
        ProcessInstance start = processInstanceBuilder.processDefinitionKey("my-process").
                variables(variables).transientVariables(transientVariables).start();
        //修改参数
        activitiRule.getRuntimeService().setVariable(start.getId(),"key1","value_1111");
        Task task = activitiRule.getTaskService().createTaskQuery().processInstanceId(start.getId()).singleResult();

        Map<String ,String> properties = Maps.newHashMap();
        properties.put("fkey1","fvalue1");
        properties.put("key2","value_222");

        //提交表单
        activitiRule.getFormService().submitTaskFormData(task.getId(),properties);

        //获取流程实例
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().listPage(0, 100);
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            logger.info("historicProcessInstance = {} ",historicProcessInstance);
        }

        //获取执行流程实例节点对象
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            logger.info("historicActivityInstance = {} ",historicActivityInstance);
        }

        //获取流程task
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().listPage(0, 100);
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            logger.info(" historicTaskInstance = {} ",historicTaskInstance);
        }

        //获取变量
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            logger.info("historicVariableInstance = {} ",historicVariableInstance);
        }

        //获取historicDetail
        List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery().listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            logger.info("historicDetail = {} ",historicDetail);
        }

        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService.createProcessInstanceHistoryLogQuery(start.getId())
                .includeVariables()
                .includeComments()
                .includeFormProperties()
                .includeTasks()
                .includeActivities()
                .includeVariableUpdates().singleResult();
        List<HistoricData> historicData = processInstanceHistoryLog.getHistoricData();
        for (HistoricData historicDatum : historicData) {
            logger.info("historicDatum = {} ",historicDatum);
        }

        historyService.deleteHistoricProcessInstance(start.getId());

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(start.getId()).singleResult();

        logger.info("historicProcessInstance = {} ",historicProcessInstance);


    }


}
