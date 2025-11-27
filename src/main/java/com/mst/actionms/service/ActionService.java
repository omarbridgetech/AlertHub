package com.mst.actionms.service;

import com.mst.actionms.dto.ActionExecutionMessage;
import com.mst.actionms.dto.ActionRequest;
import com.mst.actionms.kafka.ActionKafkaProducer;
import com.mst.actionms.model.Action;
import com.mst.actionms.model.Runday;
import com.mst.actionms.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ActionService {
    private static final Logger logger = LoggerFactory.getLogger(ActionService.class);
//    private final ActionKafkaProducer actionKafkaProducer;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private  ActionKafkaProducer actionKafkaProducer;

    public  Action createAction(ActionRequest request, String userId){
        Action action = new Action();
        action.setName(request.getName());
        action.setTo(request.getTo());
        action.setActionType(request.getActionType());
        action.setRunOnTime(request.getRunOnTime());
        action.setRunOnDay(request.getRunOnDay());
        action.setMessage(request.getMessage());
        action.setCondition(request.getCondition());
        action.setUserId(userId);
        action.setCreateDate(LocalDateTime.now());
        action.setEnabled(true);
        action.setDeleted(false);
        action.setLastRun(null);
        action.setLastUpdate(Timestamp.valueOf(LocalDateTime.now()));
       return actionRepository.save(action);
    }
    public Action updateAction(UUID id, ActionRequest request, String userIdFromToken) {
        Action action = actionRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Action not found"));

        action.setName(request.getName());
        action.setTo(request.getTo());
        action.setActionType(request.getActionType());
        action.setRunOnTime(request.getRunOnTime());
        action.setRunOnDay(request.getRunOnDay());
        action.setMessage(request.getMessage());
        action.setCondition(request.getCondition());
        // userId, createDate, isDeleted, lastRun stay unchanged
        action.setLastUpdate(Timestamp.valueOf(LocalDateTime.now()));

        return actionRepository.save(action);
    }

    public List<Action> getAllActions(){
        return actionRepository.findAll();
    }
    public List<Action> getActionsByUserId(String userId) {
        return actionRepository.findByUserIdAndIsDeletedFalse(userId);
    }
    public Optional<Action> getActionsByID(UUID uuid) {
        return actionRepository.findById(uuid);
    }

    public void deleteAction(UUID id) {
        Action action = actionRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Action not found"));

        action.setDeleted(true);
        action.setLastUpdate(Timestamp.valueOf(LocalDateTime.now()));
        actionRepository.save(action);
    }

    public Action enableAction(UUID id){
        Action action = actionRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Action not found"));

        action.setEnabled(true);
        action.setLastUpdate(Timestamp.valueOf(LocalDateTime.now()));

        return actionRepository.save(action);
    }
    public Action disableAction(UUID id){
        Action action = actionRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Action not found"));

        action.setEnabled(false);
        action.setLastUpdate(Timestamp.valueOf(LocalDateTime.now()));

        return actionRepository.save(action);

    }

    public void processDueActions() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime nowTime = LocalTime.from(now.truncatedTo(ChronoUnit.MINUTES)); // ignore seconds
        DayOfWeek today = now.getDayOfWeek();
        Runday todayRunDay = mapDayOfWeekToRunday(today);

        List<Action> candidates = actionRepository.findByIsDeletedFalseAndIsEnabledTrue();
        for (Action action : candidates) {
            boolean dayMatches =
                    action.getRunOnDay() == Runday.All ||     // “All” runs every day
                            action.getRunOnDay() == todayRunDay;      // or exact match

            LocalTime actionTime = action.getRunOnTime().truncatedTo(ChronoUnit.MINUTES);
            boolean timeMatches = actionTime.equals(nowTime);

            if (dayMatches && timeMatches) {
                // 5. "Enqueue" the action – for now just log, later send to Processor
                sendToQueue(action);

                // 6. Update lastRun and save
                action.setLastRun(Timestamp.valueOf(now));
                actionRepository.save(action);
            }
        }
    }
    private Runday mapDayOfWeekToRunday(DayOfWeek dayOfWeek) {

        return switch (dayOfWeek) {
            case MONDAY -> Runday.Monday;
            case TUESDAY -> Runday.Tuesday;
            case WEDNESDAY -> Runday.Wednesday;
            case THURSDAY -> Runday.Thursday;
            case FRIDAY -> Runday.Friday;
            case SATURDAY -> Runday.Saturday;
            case SUNDAY -> Runday.Sunday;
        };
    }


    private void sendToQueue(Action action) {
        ActionExecutionMessage message = new ActionExecutionMessage();
        message.setActionId(action.getId());
        message.setUserId(action.getUserId());
        message.setActionType(action.getActionType());
        message.setTo(action.getTo());
        message.setMessage(action.getMessage());
        message.setCondition(action.getCondition());
        message.setRunDateTime(LocalDateTime.now());
        logger.info("Prepared ActionExecutionMessage to send to Processor via Kafka: {}", message);
        actionKafkaProducer.send(message);
    }




}
