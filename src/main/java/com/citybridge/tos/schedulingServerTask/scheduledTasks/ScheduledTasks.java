package com.citybridge.tos.schedulingServerTask.scheduledTasks;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final TaskService taskService;

    @Autowired
    public ScheduledTasks(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(fixedRate = 5_000) //(fixedRate = 300_000)
    public void reportCurrentTime() {
        taskService.Task();
    }
}




