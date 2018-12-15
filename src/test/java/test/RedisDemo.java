package test;

import cn.jxau.zw.hello.CustomRedis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CustomRedis.class, ApplicationContext.class})
public class RedisDemo {

    private static final Logger log = LoggerFactory.getLogger(RedisDemo.class);


    @Test
    public void test1(){
//        StringRedisTemplate stringRedisTemplate = new AnnotationConfigApplicationContext(CustomRedis.class).getBean(StringRedisTemplate.class);
        Jedis jedis = new Jedis("127.0.0.1", 6379);
//        jedis.set("name","zhaowei");
        log.info(jedis.isConnected() + " ; " + jedis.get("name"));
        log.info(">>>>>>>>>>>>>>>>>>>>" );
    }
}
