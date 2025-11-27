package com.mst.actionms.controller;

import com.mst.actionms.dto.ActionRequest;
import com.mst.actionms.model.Action;
import com.mst.actionms.service.ActionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/actions")
// http://localhost:808//api/actions/create
public class ActionController {

    @Autowired
    private ActionService actionService;

//    @PostMapping("/create")
//    public ResponseEntity<Action> createAction(@RequestBody @Valid ActionRequest request) {
//        Action created = actionService.createAction(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }
@PostMapping("/create")
public ResponseEntity<Action> createAction(
        @RequestBody @Valid ActionRequest request,
        @AuthenticationPrincipal Jwt jwt
) {

    Integer userIdFromToken = jwt.getClaim("user_id");
    String userId = String.valueOf(userIdFromToken);

    Action created = actionService.createAction(request, userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}



    @GetMapping("/getAll")
    public ResponseEntity<List<Action>> getAllActions(){
        List<Action> actions = actionService.getAllActions();
        return new ResponseEntity<>(actions, HttpStatus.OK);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Action> getActionsByID(@PathVariable @Valid UUID id) {
        Optional<Action> action = actionService.getActionsByID(id);
        return action.map(value -> new ResponseEntity<>(value,HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

   @GetMapping("/user/{userId}")
   public ResponseEntity<List<Action>> getActionsByUser(@PathVariable @Valid String userId) {
       List<Action> actions = actionService.getActionsByUserId(userId);
        return ResponseEntity.ok(actions);
   }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAction(@PathVariable UUID id){
        actionService.deleteAction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Action> updateAction(
            @PathVariable UUID id,
            @RequestBody @Valid ActionRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Integer userIdFromToken = jwt.getClaim("user_id");
        String userId = String.valueOf(userIdFromToken);

        Action updated = actionService.updateAction(id, request, userId);
        return ResponseEntity.ok(updated);
    }
    @PatchMapping("/{id}/enable")
    public ResponseEntity<Action> enableAction(@PathVariable UUID id) {
        Action updated = actionService.enableAction(id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Action> disableAction(@PathVariable UUID id) {
        Action updated = actionService.disableAction(id);
        return ResponseEntity.ok(updated);
    }




}
