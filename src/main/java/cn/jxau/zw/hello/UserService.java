package cn.jxau.zw.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    public void addUser(String name){

        log.info("添加user: + " + name);

    }
}
