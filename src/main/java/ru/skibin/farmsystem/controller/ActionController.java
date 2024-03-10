package ru.skibin.farmsystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
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
import ru.skibin.farmsystem.api.dto.ActionResponse;
import ru.skibin.farmsystem.api.request.action.AddActionRequest;
import ru.skibin.farmsystem.api.request.action.GetAllActionsForPeriodRequest;
import ru.skibin.farmsystem.api.request.action.UpdateActionRequest;
import ru.skibin.farmsystem.exception.common.ValidationException;
import ru.skibin.farmsystem.service.ActionService;
import ru.skibin.farmsystem.util.BindingResultUtil;

import java.util.Collection;

@RestController
@RequestMapping("/action")
@RequiredArgsConstructor
public class ActionController {
    private final ActionService actionService;

    @PostMapping
    public ResponseEntity<ActionResponse> addAction(
            @Valid @RequestBody
            AddActionRequest addActionRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) throw new ValidationException(
                BindingResultUtil.requestValidationToString(bindingResult)
        );
        return new ResponseEntity<>(
                actionService.addAction(
                        addActionRequest.getProfileId(),
                        addActionRequest.getProductId(),
                        addActionRequest.getValue(),
                        addActionRequest.getTime()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionResponse> getAction(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(
                actionService.getAction(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/period")
    public ResponseEntity<Collection<ActionResponse>> getAllActions(
            @Valid @RequestBody
            GetAllActionsForPeriodRequest request,
            @RequestParam("limit")
            @PositiveOrZero(message = "limit must be positive")
            Integer limit,
            @RequestParam("offset")
            @PositiveOrZero(message = "offset must be positive")
            Integer offset
    ) {
        return new ResponseEntity<>(
                actionService.findPeriodActions(request.getStart(), request.getEnd(), limit, offset),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/profile")
    public ResponseEntity<ActionResponse> updateActionProfileId(
            @PathVariable("id") Long id,
            @RequestParam("new-id") Long newProfileId
    ) {
        return new ResponseEntity<>(
                actionService.updateActionProfileId(id, newProfileId),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/product")
    public ResponseEntity<ActionResponse> updateActionProductId(
            @PathVariable("id") Long id,
            @RequestParam("newProfileId") Long newProductId,
            @RequestParam("value") Float value
    ) {
        return new ResponseEntity<>(
                actionService.updateActionProductId(id, newProductId, value),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ActionResponse> updateActionActualStatus(
            @PathVariable("id") Long id,
            @RequestParam("actual") Boolean isActual
    ) {
        return new ResponseEntity<>(
                actionService.updateActionActualStatus(id, isActual),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionResponse> updateAction(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateActionRequest request
    ) {
        return new ResponseEntity<>(
                actionService.updateAction(
                        id,
                        request.getProfileId(),
                        request.getProductId(),
                        request.getValue(),
                        request.getTime(),
                        request.getIsActual()
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAction(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(
                actionService.deleteAction(id),
                HttpStatus.OK
        );
    }
}