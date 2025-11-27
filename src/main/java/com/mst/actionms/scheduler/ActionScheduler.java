package com.mst.actionms.scheduler;

import com.mst.actionms.service.ActionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ActionScheduler {

    private static final Logger logger =
            LoggerFactory.getLogger(ActionScheduler.class);
    private final ActionService actionService;
    public ActionScheduler(ActionService actionService) {
        this.actionService = actionService;
    }
    // @Scheduled(cron = "0 0/30 * * * *")
    @Scheduled(cron = "0 * * * * *")
    public void runDueActionsJob() {
        logger.info("Scheduler running: checking for due actions");
        System.out.println("Scheduler running: checking for due actions...");
        actionService.processDueActions();
    }

}
