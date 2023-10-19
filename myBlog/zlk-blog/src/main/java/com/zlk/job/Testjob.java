package com.zlk.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Testjob {
    //Scheduled指定定时任务要执行的代码
    //cron = "0/5 * * * * ?"代表从什么时候开始执行，这里指从每一分钟的0秒开始，每隔5s执行一次代码
    @Scheduled(cron = "0/5 * * * * ?")
    public void testJob(){
        //要执行的代码
        System.out.println("定时任务执行 ");
    }
}
