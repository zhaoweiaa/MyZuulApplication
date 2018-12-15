package cn.jxau.zw.hello;

import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class Cat {

    public Cat() {}

    public void hello(){

        System.out.println("i am a cat");

    }
}
