package coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
 * @date 2018/10/14 15:44
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/14
 */
public class FormServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(FormServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = "my-process-form.bpmn20.xml")
    public void testIdentity(){
        FormService formService = activitiRule.getFormService();
        //获取流程文件
        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();

        //获取开始表单key
        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        logger.info("stastFormKey = {} ",startFormKey);

        //获取表单属性
        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        for (FormProperty formProperty : formProperties) {
//            formProperty.getValue()
            logger.info("FomProperty = {} ",ToStringBuilder.reflectionToString(formProperty,ToStringStyle.JSON_STYLE));
        }

        //提交表单，启动流程
        Map<String , String> properties = Maps.newHashMap();
        properties.put("message","My test message");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), properties);

        //获取task
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        //获取task表单
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> formPropertyList = taskFormData.getFormProperties();
        for (FormProperty formProperty : formPropertyList) {
            logger.info("taskFormProperty = {} ",ToStringBuilder.reflectionToString(formProperty,ToStringStyle.JSON_STYLE));
        }


        //提交表单
        formService.submitTaskFormData(task.getId(),Maps.newHashMap());



    }

}
