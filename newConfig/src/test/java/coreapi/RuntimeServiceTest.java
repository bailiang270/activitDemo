package coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
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
 * @date 2018/10/10 18:05
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/10
 */
public class RuntimeServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeServiceTest.class);

    @Rule
    public ActivitiRule activitiRule2 = new ActivitiRule();


    @Test
    @Deployment(resources = "my-process_job.bpmn20.xml")
    public void testStartProcess(){
        RuntimeService runtimeService = activitiRule2.getRuntimeService();

        RepositoryService repositoryService = activitiRule2.getRepositoryService();
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().listPage(0, 100);
        for (ProcessDefinition processDefinition:processDefinitions){
            logger.info("实例 = {} ", processDefinition);
        }
        Map<String ,Object> variables = Maps.newHashMap();
        variables.put("key1","value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        logger.info("processInstance = {} ",processInstance);
    }


    @Test
    @Deployment(resources = "my-process_job.bpmn20.xml")
    public void testVariables(){
        RuntimeService runtimeService = activitiRule2.getRuntimeService();

        //设置参数
        Map<String ,Object> variables = Maps.newHashMap();
        variables.put("key1","value1");
        variables.put("key2","value2");
        //带参数启动
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        logger.info("processInstance = {} ",processInstance);
        //修改OR添加参数
        runtimeService.setVariable(processInstance.getId(),"key3","value3");
        runtimeService.setVariable(processInstance.getId(),"key2","value2_1");
        //获取参数
        Map<String, Object> variables1 = runtimeService.getVariables(processInstance.getId());
        logger.info(" variables1 = {}",variables1);
    }



    @Test
    @Deployment(resources = "my-process_job.bpmn20.xml")
    public void testProcessInstanceQuery(){
        RuntimeService runtimeService = activitiRule2.getRuntimeService();

        //实例：ProcessInstance表示一次工作流业务的数据实体
        //执行流：Execution表示流程实例中具体的执行路径

        //启动
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        logger.info("processInstance = {} ",processInstance);

        //获取实例
        ProcessInstance processInstance1 = runtimeService
                .createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        logger.info("Instance = {} ",processInstance1);

        //获取执行流
        List<Execution> executions = runtimeService.createExecutionQuery().listPage(0, 100);
        for (Execution execution : executions) {
            logger.info("execution = {} ",execution);
        }


    }



    @Test
    @Deployment(resources = "my-process_trigger.bpmn20.xml")
    public void testTrigger(){
        RuntimeService runtimeService = activitiRule2.getRuntimeService();

        //启动
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        logger.info("processInstance = {} ",processInstance);

        //获取执行流
        Execution execution = runtimeService.createExecutionQuery().activityId("someTask").singleResult();

//        runtimeService.cre
        logger.info("execution = {} ",execution);
        //执行触发
        runtimeService.trigger(execution.getId());


        runtimeService.createExecutionQuery().startedBy("");
        Execution executiona = runtimeService.createExecutionQuery().singleResult();

        execution = runtimeService.createExecutionQuery().activityId("someTask").singleResult();
        logger.info("execution = {} ",execution);

    }


    /**
     * 信号接收
     */
    @Test
//    @Deployment(resources = "my-process_signal_received.bpmn20.xml")
    public void testSignalEventReceived(){
        RuntimeService runtimeService = activitiRule2.getRuntimeService();

        //启动
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        logger.info("processInstance = {} ",processInstance);

        //查询--触发信号
        Execution execution = runtimeService.createExecutionQuery().signalEventSubscriptionName("my-signal").singleResult();
        logger.info("execution = {} ",execution);


        List<Task> tasks = activitiRule2.getTaskService().createTaskQuery().listPage(0, 100);
        logger.info("size = {} ",tasks);

        //执行
        runtimeService.signalEventReceived("my-signal");

        execution = runtimeService.createExecutionQuery().signalEventSubscriptionName("my-signal").singleResult();
        logger.info("execution = {} ",execution);

    }




}
