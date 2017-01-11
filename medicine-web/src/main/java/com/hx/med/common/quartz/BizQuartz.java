package com.hx.med.common.quartz;


import com.hx.med.common.cache.RedisClusterCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 业务相关的作业调度.
 秒 0-59 , - * /
 分 0-59 , - * /
 小时 0-23 , - * /
 日期 1-31 , - * ? / L W C
 月份 1-12 或者 JAN-DEC , - * /
 星期 1-7 或者 SUN-SAT , - * ? / L C #
 年（可选） 留空, 1970-2099 , - * /
 *  字符代表所有可能的值
 /  字符用来指定数值的增量
 L  字符仅被用于天（月）和天（星期）两个子表达式，表示一个月的最后一天或者一个星期的最后一天
 6L 可以表示倒数第６天
 * @author yingjun
 * @since 1.6
 */
@Component
public class BizQuartz {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisClusterCache redisClusterCache;

}
