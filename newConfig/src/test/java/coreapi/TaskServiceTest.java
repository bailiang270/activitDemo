package coreapi;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
public class TaskServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();


    @Test
    @Deployment(resources = "my-process-task.bpmn20.xml")
    public void testTaskService(){

        //设置参数并启动流程
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","My Test Message!!");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process",variables);

        //获取task
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        logger.info("task = {} ",ToStringBuilder.reflectionToString(task,ToStringStyle.JSON_STYLE));
        logger.info("task.description = {} ", task.getDescription());

        //设置参数，本地参数
        taskService.setVariable(task.getId(),"key1","value1");
        taskService.setVariableLocal(task.getId(),"Localkey1","value1");

        //获取参数
        //1、task获取参数
        Map<String, Object> variables1 = taskService.getVariables(task.getId());
        //2、task获取本地参数
        Map<String, Object> variablesLocal = taskService.getVariablesLocal(task.getId());
        //3、获取执行流获取参数
        Map<String, Object> variables2 = activitiRule.getRuntimeService().getVariables(task.getExecutionId());

        logger.info("task获取参数 = {} ",variables1);
        logger.info("task获取本地参数 = {} ",variablesLocal);
        logger.info("获取执行流获取参数 = {} ",variables2);

        //设置销毁变量
        Map<String , Object> completeVar = Maps.newConcurrentMap();
        completeVar.put("cKey1","cValue1");
        taskService.complete(task.getId(),completeVar);

        Task task1 = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        logger.info("task1 = {} ", task1);

    }

    @Test
    @Deployment(resources = "my-process-task.bpmn20.xml")
    public void testTaskServiceUser(){

        //设置参数并启动流程
        Map<String,Object> variables = Maps.newHashMap();
        variables.put("message","My Test Message!!");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process",variables);

        //获取task
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        logger.info("task = {} ",ToStringBuilder.reflectionToString(task,ToStringStyle.JSON_STYLE));
        logger.info("task.description = {} ", task.getDescription());

        //设置拥有人(流程发起人)
        taskService.setOwner(task.getId(),"owner");
//        taskService.setAssignee("");

        List<Task> sddd = taskService.createTaskQuery().taskAssignee("sddd").listPage(0, 100);

        //设置办理人(不推荐方式)
//        taskService.setAssignee(task.getId(),"bai");


        //候选人和代办人的区别：候选人可以有多个，代办人只有一个，并且候选人可以成为代办人
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("bai").listPage(0, 100);
        for (Task task1 : taskList) {
            try {
                taskService.claim(task1.getId(),"bai");
                taskService.addCandidateGroup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //查询出task与用户组之间的关系
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : identityLinksForTask) {
            logger.info("identityLink = {} ",identityLink);
        }

        //获取代办人为“bai”的task
        List<Task> bai = taskService.createTaskQuery().taskAssignee("bai").listPage(0, 100);

        for (Task task1 : bai) {
            Map<String , Object> vars = Maps.newHashMap();
            vars.put("ckey1","cvalue1");
            //办理
            taskService.complete(task1.getId(),vars);
//            taskService.comp
        }
        bai  = taskService.createTaskQuery().taskAssignee("bai").listPage(0,100);
        logger.info("是否存在办理人为bai的task = {} ",CollectionUtils.isEmpty(bai));

    }



    @Test
    @Deployment(resources = "my-process-task.bpmn20.xml")
    public void testTaskAttachment() {

        //设置参数并启动流程
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "My Test Message!!");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);

        //获取task
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        logger.info("task = {} ", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        logger.info("task.description = {} ", task.getDescription());

        //为task创建附件
        taskService.createAttachment("url",task.getId(),task.getProcessInstanceId(),
                "name","desc","url/test/bai.png");

        //查询task的所有附件
        List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());
        for (Attachment taskAttachment : taskAttachments) {
            logger.info("taskAttachment = {} ",ToStringBuilder.reflectionToString(taskAttachment,ToStringStyle.JSON_STYLE));
        }
    }


    @Test
    @Deployment(resources = "my-process-task.bpmn20.xml")
    public void testTaskComment() {

        //设置参数并启动流程
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "My Test Message!!");
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", variables);

        //获取task
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        logger.info("task = {} ", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        logger.info("task.description = {} ", task.getDescription());

        //为task创建备注
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"new message 1");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"new message 2");


        //1、Comment只记录备注信息
        //2、Event 记录task所有操作信息

        //查询task的所有备注
        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for (Comment taskComment : taskComments) {
            logger.info("taskComment = {} ",ToStringBuilder.reflectionToString(taskComment,ToStringStyle.JSON_STYLE));
        }

        //查询事件记录
        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event taskEvent : taskEvents) {
            logger.info("taskEvent = {} ",ToStringBuilder.reflectionToString(taskEvent,ToStringStyle.JSON_STYLE));
        }



    }

}
