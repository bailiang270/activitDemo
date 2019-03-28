package dbentity;

import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description:
 *
 * @author liang.bai@hand-china.com
 * @date 2018/10/16 11:08
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/16
 */
public class DbConfigTest {

    private static final Logger logger = LoggerFactory.getLogger(DbConfigTest.class);

    @Test
    public void testDbConfig(){

        ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();

        ManagementService managementService = processEngine.getManagementService();


    }

}
