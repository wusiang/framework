package com.xianmao.utils;

/**
 * @ClassName PropertyUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 22:45
 * @Version 1.0
 */
public class PropertyUtil {

    private PropertyUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }
}
