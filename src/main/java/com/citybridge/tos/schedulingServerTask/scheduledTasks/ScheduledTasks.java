package com.citybridge.tos.schedulingServerTask.scheduledTasks;



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

    /**
     * Execute Task service every 5 seconds (minutes)
     *
     *
     */
    @Scheduled(fixedRate = 5_000) //(fixedRate = 300_000)
    public void reportCurrentTime() {
        taskService.task();
    }
}




