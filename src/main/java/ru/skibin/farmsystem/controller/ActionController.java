package ru.skibin.farmsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
import ru.skibin.farmsystem.api.request.action.AddActionRequest;
import ru.skibin.farmsystem.api.request.action.PeriodRequest;
import ru.skibin.farmsystem.api.request.action.UpdateActionRequest;
import ru.skibin.farmsystem.api.response.ActionResponse;
import ru.skibin.farmsystem.exception.common.ValidationException;
import ru.skibin.farmsystem.service.ActionService;

import java.util.Collection;

@RestController
@RequestMapping("/action")
@RequiredArgsConstructor
public class ActionController {
    private final ActionService actionService;

    /**
     * Adding action to repository
     *
     * @param addActionRequest request with action data
     * @param bindingResult    request validation data
     * @return Http response with added action data
     */
    @Operation(summary = "Adding action to repository")
    @PostMapping
    public ResponseEntity<ActionResponse> addAction(
            @Valid @RequestBody
            AddActionRequest addActionRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return new ResponseEntity<>(
                actionService.addAction(addActionRequest),
                HttpStatus.OK
        );
    }

    /**
     * Getting action from repository
     *
     * @param id numerical identifier of required action
     * @return Http response with required action data
     */
    @GetMapping("/{id}")
    @Operation(summary = "Getting action from repository")
    public ResponseEntity<ActionResponse> getAction(
            @PathVariable("id")
            @Validated
            @Positive
            Long id
    ) {
        return new ResponseEntity<>(
                actionService.getAction(id),
                HttpStatus.OK
        );
    }

    /**
     * Receiving actions created during the periodR
     *
     * @param limit         pagination limit
     * @param offset        pagination offset
     * @param request       request with period data
     * @param bindingResult request validation data
     * @return Http response with data of added actions
     */
    @PostMapping("/period")
    @Operation(summary = "Receiving actions created during the period")
    public ResponseEntity<Collection<ActionResponse>> getPeriodActions(
            @RequestParam("limit")
            @Validated
            @PositiveOrZero(message = "limit must be positive")
            Integer limit,
            @RequestParam("offset")
            @Validated
            @PositiveOrZero(message = "offset must be positive")
            Integer offset,
            @Valid
            @RequestBody
            PeriodRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return new ResponseEntity<>(
                actionService.findPeriodActions(request, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Replace the profile id of the person responsible for the action
     *
     * @param id           action identifier
     * @param newProfileId new profile identifier
     * @return Http response with updated action data
     */
    @PatchMapping("/{id}/profile")
    @Operation(summary = "Replace the profile id of the person responsible for the action")
    public ResponseEntity<ActionResponse> updateActionProfileId(
            @PathVariable("id")
            @Validated
            @Positive
            Long id,
            @RequestParam("new-id")
            Long newProfileId
    ) {
        return new ResponseEntity<>(
                actionService.updateActionProfileId(id, newProfileId),
                HttpStatus.OK
        );
    }

    /**
     * Replace the product id for the action
     *
     * @param id           action identifier
     * @param newProductId new product identifier
     * @param value        new product value
     * @return Http response with updated action data
     */
    @PatchMapping("/{id}/product")
    @Operation(summary = "Replace the product id for the action")
    public ResponseEntity<ActionResponse> updateActionProductId(
            @PathVariable("id")
            @Validated
            @Positive
            Long id,
            @RequestParam("newProfileId") Long newProductId,
            @RequestParam("value") Float value
    ) {
        return new ResponseEntity<>(
                actionService.updateActionProductId(id, newProductId, value),
                HttpStatus.OK
        );
    }

    /**
     * Updating of the actuality status
     *
     * @param id       action identifier
     * @param isActual actual boolean status
     * @return Http response with updated action data
     */
    @PatchMapping("/{id}/actual-status")
    @Operation(summary = "Updating of the actuality status")
    public ResponseEntity<ActionResponse> updateActionActualStatus(
            @PathVariable("id")
            @Validated
            @Positive
            Long id,
            @RequestParam("actual") Boolean isActual
    ) {
        return new ResponseEntity<>(
                actionService.updateActionActualStatus(id, isActual),
                HttpStatus.OK
        );
    }

    /**
     * Updating action
     *
     * @param id            action identifier
     * @param request       request with new action data
     * @param bindingResult request validation data
     * @return Http response with updated action data
     */
    @PutMapping("/{id}")
    @Operation(summary = "Updating action")
    public ResponseEntity<ActionResponse> updateAction(
            @PathVariable("id")
            @Validated
            @Positive
            Long id,
            @Valid @RequestBody
            UpdateActionRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return new ResponseEntity<>(
                actionService.updateAction(id, request),
                HttpStatus.OK
        );
    }

    /**
     * Deleting or deactivate action
     *
     * @param id action identifier
     * @return Http response, true - if action was deleted or deactivated
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting or deactivate action")
    public ResponseEntity<Boolean> deleteAction(
            @PathVariable("id")
            @Validated
            @Positive
            Long id
    ) {
        return new ResponseEntity<>(
                actionService.deleteAction(id),
                HttpStatus.OK
        );
    }
}
