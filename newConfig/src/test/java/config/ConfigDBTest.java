package config;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description:
 *
 * @author liang.bai@hand-china.com
 * @date 2018/10/9 15:18
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/9
 */
public class ConfigDBTest {

    private static final Logger logger = LoggerFactory.getLogger(ConfigDBTest.class);

    @Test
    public void testConfig(){

        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        logger.info("configuration = {}",processEngineConfiguration);
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        logger.info("获取流程引擎 {}", processEngine.getName());

        processEngine.close();

    }

    @Test
    public void testConfig2(){

        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");
        logger.info("configuration = {}",processEngineConfiguration);
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        logger.info("获取流程引擎 {}", processEngine.getName());

        processEngine.close();

    }

}
