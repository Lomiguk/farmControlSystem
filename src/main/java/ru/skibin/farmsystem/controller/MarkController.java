package ru.skibin.farmsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skibin.farmsystem.api.request.action.PeriodRequest;
import ru.skibin.farmsystem.api.request.mark.AddMarkRequest;
import ru.skibin.farmsystem.api.request.mark.UpdateMarkRequest;
import ru.skibin.farmsystem.api.response.MarkResponse;
import ru.skibin.farmsystem.service.MarkService;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/profile/")
@RequiredArgsConstructor
@Tag(name = "Mark")
public class MarkController {
    private final MarkService markService;

    /**
     * Adding mark data to repository
     *
     * @param addMarkRequest Request with mark data
     * @param bindingResult  Request validation data
     * @return Http response with mark response model
     */
    @Operation(summary = "Adding mark data to repository")
    @PostMapping("{id}/mark")
    public ResponseEntity<MarkResponse> add(
            @Validated
            @Positive
            Long profileId,
            @Valid
            @RequestBody
            AddMarkRequest addMarkRequest,
            BindingResult bindingResult
    ) {
        return new ResponseEntity<>(
                markService.addMark(bindingResult, addMarkRequest, profileId),
                HttpStatus.OK
        );
    }

    /**
     * Getting the mark data from repository
     *
     * @param markId mark numerical identifier
     * @return Http response with mark response model
     */
    @Operation(summary = "Getting the mark data from repository")
    @GetMapping("mark/{id}")
    public ResponseEntity<MarkResponse> get(
            @Positive
            @Validated
            @PathVariable("id")
            Long markId
    ) {
        return new ResponseEntity<>(
                markService.getMark(markId),
                HttpStatus.OK
        );
    }

    /**
     * Getting marks of profile
     *
     * @param profileId Profile numerical identifier
     * @param limit     Pagination's limit
     * @param offset    Pagination's offset
     * @return Http response with collection of mark response models
     */
    @Operation(summary = "Getting marks of profile")
    @GetMapping("{id}/mark")
    public ResponseEntity<Collection<MarkResponse>> findByProfile(
            @Positive
            @Validated
            @PathVariable("id")
            Long profileId,
            @Positive
            @Validated
            @RequestParam("limit")
            Integer limit,
            @Validated
            @PositiveOrZero
            @RequestParam("offset")
            Integer offset
    ) {
        return new ResponseEntity<>(
                markService.findMarksByProfile(profileId, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Getting marks by day
     *
     * @param date   Grading day
     * @param limit  Pagination's limit
     * @param offset Pagination's offset
     * @return Http response with collection of mark response models
     */
    @Operation(summary = "Getting marks by day")
    @GetMapping("mark/day")
    public ResponseEntity<Collection<MarkResponse>> findByDay(
            @NotNull
            @Validated
            @RequestParam("date")
            LocalDate date,
            @Positive
            @Validated
            @RequestParam("limit")
            Integer limit,
            @Validated
            @PositiveOrZero
            @RequestParam("offset")
            Integer offset
    ) {
        return new ResponseEntity<>(
                markService.findMarksByDay(date, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Getting marks by day
     *
     * @param periodRequest Request with period data
     * @param limit         Pagination's limit
     * @param offset        Pagination's offset
     * @return Http response with collection of mark response models
     */
    @Operation(summary = "Getting marks by day")
    @GetMapping("mark/period")
    public ResponseEntity<Collection<MarkResponse>> findByPeriod(
            @Validated
            PeriodRequest periodRequest,
            @Positive
            @Validated
            @RequestParam("limit")
            Integer limit,
            @Validated
            @PositiveOrZero
            @RequestParam("offset")
            Integer offset
    ) {
        return new ResponseEntity<>(
                markService.findMarksByPeriod(periodRequest, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Updating mark
     *
     * @param markId            Mark numerical identifier
     * @param updateMarkRequest Request with new data of mark
     * @param bindingResult     Request validation data
     * @return Http response with mark response model
     */
    @Operation(summary = "Updating mark")
    @PutMapping("mark/{id}")
    public ResponseEntity<MarkResponse> updateMark(
            @Positive
            @Validated
            @PathVariable("id")
            Long markId,
            @Valid
            @RequestBody
            UpdateMarkRequest updateMarkRequest,
            BindingResult bindingResult
    ) {
        return new ResponseEntity<>(
                markService.updateMark(bindingResult, markId, updateMarkRequest),
                HttpStatus.OK
        );
    }

    /**
     * Deleting mark
     *
     * @param markId Mark numerical identifier
     * @return Http response with boolean value, true - if mark is deleted
     */
    @Operation(summary = "Deleting mark")
    @DeleteMapping("mark/{id}")
    public ResponseEntity<Boolean> deleteMark(
            @Positive
            @Validated
            @PathVariable("id")
            Long markId
    ) {
        return new ResponseEntity<>(
                markService.deleteMark(markId),
                HttpStatus.OK
        );
    }
}
