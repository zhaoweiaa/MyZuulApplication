package cn.jxau.zw.hello;

import cn.jxau.zw.MyZuulApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.cloud.env.EnvironmentUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * 练习SpringBootTest框架
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyTestBeanConfiguraiton.class, MyZuulApplication.class,Cat.class},
        properties = {"app.version=1.1.0"})
@AutoConfigureMockMvc
public class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    ApplicationContext context;

    @Autowired
    private  UserService userService;

    @Autowired
    private ConfigurableEnvironment environment;

    @Before
    public void init(){
        EnvironmentTestUtils.addEnvironment(environment, "app.admin.user=zhangsan");
    }


    /**
     * SpringBoot获取spring 容器，获取userService Bean
     * @throws Exception
     */
    @Test
    public void addUser() throws Exception {

       Assert.assertNotNull(context.getBean(UserService.class));

       log.info("find bean userService");

       userService.addUser("zhaowei");
    }

    /**
     * 获取测试Bean （@TestComponent）,测试容器(@TestConfiguration)
     */
    @Test
    public void testCatbean(){

        Cat cat = context.getBean(Cat.class);

        cat.hello();

        Runnable run = context.getBean(Runnable.class);

        run.run();

    }

    @Autowired
    Environment env;

    /**
     * SpringBoot profiles 测试
     */
    @Test
    public void testValue(){

        log.info(env.toString());

        log.info(env.getProperty("developer.name"));

        Arrays.stream(env.getActiveProfiles()).forEach( profileName ->log.info(profileName));

    }


    /**
     * 获取自定义环境变量
     */
    @Test
    public void readEnvProperties(){

        log.info(environment.getProperty("app.version"));

        log.info(environment.getProperty("app.admin.user"));

        log.info(env.getProperty("app.version") + ";" + env.getProperty("app.admin.user"));

    }

    @Autowired
    MockMvc mockMvc;

    /**
     * 模拟/hello请求
     */
    @Test
    public void testGreetController(){
        try {
            mockMvc.perform(MockMvcRequestBuilders.request("GET", URI.create("/hello")))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("赵伟"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}