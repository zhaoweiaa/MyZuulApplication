package cn.jxau.zw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * 用于SpringBootTest 测试
 */
@RestController
public class GreetController {

    private Logger logger = LoggerFactory.getLogger(GreetController.class);

    @GetMapping("/hello")
    public Object greet(){
        return "赵伟";
//        return Instant.now() + "------赵伟";
    }
}
