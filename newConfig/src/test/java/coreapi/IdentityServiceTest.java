package coreapi;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * description:
 *
 * @author liang.bai@hand-china.com
 * @date 2018/10/14 15:44
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/14
 */
public class IdentityServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(IdentityServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testIdentity(){

        IdentityService identityService = activitiRule.getIdentityService();
        //创建用户
        User user1 = identityService.newUser("user1");
        user1.setEmail("user1@qq.com");
        User user2 = identityService.newUser("user2");
        user2.setEmail("user2@qq.com");
//        user2.
        identityService.saveUser(user1);
        identityService.saveUser(user2);

        //创建用户组
        Group group1 = identityService.newGroup("group1");
        identityService.saveGroup(group1);

        //用户与组建立关系
        identityService.createMembership("user1","group1");
        identityService.createMembership("user2","group1");

        List<User> userList = identityService.createUserQuery().memberOfGroup("group1").listPage(0, 100);
        for (User user : userList) {
            logger.info("user  =  {} ",ToStringBuilder.reflectionToString(user,ToStringStyle.JSON_STYLE));
        }

        List<Group> groups = identityService.createGroupQuery().groupMember("user1").listPage(0, 100);
        for (Group group : groups) {
            logger.info("group = {} ",ToStringBuilder.reflectionToString(group,ToStringStyle.JSON_STYLE));
        }




    }

}
