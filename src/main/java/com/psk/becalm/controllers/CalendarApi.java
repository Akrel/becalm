package com.psk.becalm.controllers;

import com.psk.becalm.services.CalendarService;
import com.psk.becalm.transport.dto.request.calendar.AddCalendarTaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "calendar")
public class CalendarApi {
    @Autowired
    private CalendarService calendarService;


    @GetMapping("/allInMonth")
    public ResponseEntity<?> getAllTaskInMonth(@RequestParam String month) {

        return null;
    }

    @PutMapping("/addNewTask")
    public ResponseEntity<?> addNewTask(@RequestBody AddCalendarTaskDto taskDto) {
        return null;
    }


    @DeleteMapping("/removeTask")
    public ResponseEntity<?> removeTask(@RequestParam String uuid) {
        return null;
    }


}
