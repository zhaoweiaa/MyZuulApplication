package cn.jxau.zw.hello;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
public class MyTestBeanConfiguraiton {

    @Bean
    public Runnable runnable(){

        return ()-> System.out.println("i am a runnable bean");

    }

    @Profile("dev")
    @Bean
    public Runnable runnableOne(){
        return ()-> System.out.println("it's a dev bean");
    }

    @Profile("hello")
    @Bean
    public Runnable runnableTwo(){
        return ()-> System.out.println("it's a dev bean");
    }

}
