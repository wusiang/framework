package com.xianmao.common.redo;

import com.xianmao.common.redo.bean.RedoTask;

/**
 * TODOList:
 * 待优化需求
 * 1.引入线程池提高补偿任务执行速度
 * 2.补偿任务的优先级
 * 3.饥饿策略，补偿任务查出来后可连续执行，默认情况下只有数据库轮询查出来时执行一次
 * 4.退避策略，在第三点上加上退避策略（这个优先级不高，因为入库已经有延迟了，这个退避是否仍有意义）
 *
 * author:xuyaokun_kzx
 * date:2021/10/27
 * desc:
*/
public class RedoTodoList {

    public static void main(String[] args) {
        RedoTask redoTask = RedoTask.newBuilder("").maxAttempts(3).build();
        System.out.println(redoTask);
    }

    /*
        D:\maven\apache-maven-3.6.3-bin\apache-maven-3.6.3\conf\settings.xml
     */
}
