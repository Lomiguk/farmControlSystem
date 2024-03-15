package ru.skibin.farmsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skibin.farmsystem.api.request.task.AddTaskRequest;
import ru.skibin.farmsystem.api.request.task.UpdateTaskRequest;
import ru.skibin.farmsystem.api.response.TaskResponse;
import ru.skibin.farmsystem.service.TaskService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Tag(name = "Task")
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Adding task")
    @PostMapping
    public ResponseEntity<TaskResponse> add(
            @Valid
            @RequestBody
            AddTaskRequest addTaskRequest,
            BindingResult bindingResult
    ) {
        return new ResponseEntity<>(
                taskService.add(bindingResult, addTaskRequest),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Getting the task")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> get(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(
                taskService.get(id),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Updating the task's collected value")
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> addValue(
            @PathVariable("id") Long id,
            @RequestParam("value") Float additionalValue
    ) {
        return new ResponseEntity<>(
                taskService.updateCollectedValue(id, additionalValue),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Getting tasks by profile")
    @GetMapping("/profile/{id}")
    public ResponseEntity<Collection<TaskResponse>> getByProfile(
           @PathVariable("id") Long profileId
    ) {
        return new ResponseEntity<>(
                taskService.getProfilesTasks(profileId),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Updating the task")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            UpdateTaskRequest updateTaskRequest,
            BindingResult bindingResult
    ) {
        return new ResponseEntity<>(
                taskService.update(bindingResult, id, updateTaskRequest),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Deleting the task")
    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> delete(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(
                taskService.delete(id),
                HttpStatus.OK
        );
    }
}
