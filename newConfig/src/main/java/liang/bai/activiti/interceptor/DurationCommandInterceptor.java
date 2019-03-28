package liang.bai.activiti.interceptor;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description:  计算时间拦截器
 *
 * @author liang.bai@hand-china.com
 * @date 2018/10/10 15:04
 * lastUpdateBy: liang.bai@hand-china.com
 * lastUpdateDate: 2018/10/10
 */
public class DurationCommandInterceptor extends AbstractCommandInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DurationCommandInterceptor.class);

    @Override
    public <T> T execute(CommandConfig config, Command<T> command) {
        long start = System.currentTimeMillis();
        try{
            return this.next.execute(config,command);
        }finally {
            long duration = System.currentTimeMillis()-start;
            logger.info("{} 执行时间 = {} ",command.getClass().getSimpleName(),duration);
        }
    }
}
