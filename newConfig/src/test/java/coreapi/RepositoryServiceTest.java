package coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * description:
 *
 * @author liang.bai@hand-china.com
 * @date 2018/10/10 18:05
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/10
 */
public class RepositoryServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();


    @Rule
    public ActivitiRule activitiRule1 = new ActivitiRule();

    @Test
    public void testRepository(){
        //资源文件对象
        //挂起，重启，获取model
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        //添加不是文件xml
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.name("测试部署文件").addClasspathResource("my-process.bpmn20.xml");

        //部署
        Deployment deploy = deployment.deploy();
        logger.info("deploy = {} "  ,deploy);

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        Deployment deployment1 = deploymentQuery.deploymentId(deploy.getId()).singleResult();

        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().deploymentId(deployment1.getId()).listPage(0, 100);
        for (ProcessDefinition processDefinition:processDefinitions){
            logger.info("processDefinition = {} ,version = {} , key = {} , id = {} ",
                    processDefinition,processDefinition.getVersion(),processDefinition.getKey(),processDefinition.getId());
        }
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testSuspend(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        logger.info("processDefinition.di = {} ", processDefinition.getId());

        //挂起(挂起之后不能被启动)
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());

        try {
            logger.info("开始启动");
            activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
            logger.info("成功启动");
        } catch (Exception e) {
            logger.info("启动失败");
            logger.info(e.getMessage(),e);
        }

        //(挂起激活)启动
        repositoryService.activateProcessDefinitionById(processDefinition.getId());


        logger.info("开始启动");
        activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
        logger.info("成功启动");


    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCandidateStarter(){
        RepositoryService repositoryService = activitiRule1.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        logger.info("processDefinition.di = {} ", processDefinition.getId());

        //指定用户
        repositoryService.addCandidateStarterUser(processDefinition.getId(),"user");
        //指定组
        repositoryService.addCandidateStarterGroup(processDefinition.getId(),"groupM");
        //查询规则
        List<IdentityLink> identityLinksForProcessDefinition = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());

        for (IdentityLink identityLink:identityLinksForProcessDefinition){
            logger.info("identityLink = {} ",identityLink);
        }

        //删除用户组
        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(),"groupM");
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(),"user");



    }

}
