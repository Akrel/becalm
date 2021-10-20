package com.psk.becalm.controllers;

import com.psk.becalm.model.entities.CalendarTask;
import com.psk.becalm.services.CalendarService;
import com.psk.becalm.services.UserDetailsImpl;
import com.psk.becalm.services.UserException;
import com.psk.becalm.transport.converters.CalendarTaskConverter;
import com.psk.becalm.transport.dto.request.calendar.CalendarTaskDto;
import com.psk.becalm.transport.dto.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Secured("USER")
@Slf4j
@RequestMapping(value = "/calendar")
public class CalendarApi {
    @Autowired
    private CalendarService calendarService;


    @GetMapping("allInMonth/{month}")
    public ResponseEntity<?> getAllTaskInMonth(@PathVariable String month) {
        UserDetailsImpl principal = getPrincipal();
        log.info(String.format("User %s load task in month %s", principal.getUsername(), month));
        List<CalendarTask> calendarTaskInMonth = calendarService.findCalendarTaskInMonth(principal.getUserId(), Integer.parseInt(month));

        return ResponseEntity.ok(calendarTaskInMonth.stream().map(CalendarTaskConverter::toDto).collect(Collectors.toList()));
    }

    @PutMapping("/addNewTask")
    public ResponseEntity<?> addNewTask(@RequestBody CalendarTaskDto taskDto) {
        UserDetailsImpl principal = getPrincipal();

        CalendarTask calendarTask = calendarService.addTaskForUser(taskDto, principal.getUserId());

        if (calendarTask == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Cannot add task"));

        return ResponseEntity.ok(calendarTask);
    }

    @DeleteMapping("/removeTask/{taskUuid}")
    public ResponseEntity<?> removeTask(@PathVariable String taskUuid) {
        UserDetailsImpl principal = getPrincipal();

        try {
            calendarService.removeUserTask(taskUuid, principal.getUserId());
        } catch (UserException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(String.format("Cannot remove with id: %s", taskUuid)));
        }
        return ResponseEntity.ok("Removed");
    }

    private UserDetailsImpl getPrincipal() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
