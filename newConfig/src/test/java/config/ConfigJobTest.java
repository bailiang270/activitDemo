package config;

import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * description:
 *
 * @author liang.bai@hand-china.com
 * @date 2018/10/9 19:24
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/9
 */
public class ConfigJobTest {


    private static final Logger logger = LoggerFactory.getLogger(ConfigJobTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_Job.cfg.xml");

    @Test
    @Deployment(resources = {"my-process_job.bpmn20.xml"})
    public void test() throws InterruptedException {
        logger.info("start");
        List<Job> jobs = activitiRule.getManagementService().createTimerJobQuery().listPage(0, 100);
        for(Job job:jobs){
            logger.info("定时任务 = {} ， 默认重试次数 = {}",job,job.getRetries());
        }
        logger.info("job.size = {} " , jobs.size());
        Thread.sleep(1000*100);
        logger.info("end");


    }

}
