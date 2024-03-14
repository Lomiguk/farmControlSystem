package ru.skibin.farmsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skibin.farmsystem.api.request.other.WorkResult;
import ru.skibin.farmsystem.api.request.other.Worker;
import ru.skibin.farmsystem.api.request.action.PeriodRequest;
import ru.skibin.farmsystem.api.response.ActionResponse;
import ru.skibin.farmsystem.api.response.WorkerWithResult;
import ru.skibin.farmsystem.service.StatisticService;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    /**
     * Getting all actions by day
     *
     * @param day    Day
     * @param limit  Limit for pagination
     * @param offset Offset for pagination
     * @return Http response with collection of action response model
     */
    @Operation(summary = "Getting all actions by day")
    @GetMapping("/action")
    public ResponseEntity<Collection<ActionResponse>> getAllActionsByDay(
            @RequestParam("day") LocalDate day,
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset
    ) {
        return new ResponseEntity<>(
                statisticService.getAllActionsByDay(day, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Getting all actions by period
     *
     * @param periodRequest Request with period date
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Http response with collection of action response model
     */
    @Operation(summary = "Getting all actions by period")
    @PostMapping("/action")
    public ResponseEntity<Collection<ActionResponse>> getAllActionsByPeriod(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @Valid
            @RequestBody
            PeriodRequest periodRequest,
            BindingResult bindingResult
    ) {
        return new ResponseEntity<>(
                statisticService.getAllActionsByPeriod(bindingResult, periodRequest, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Getting all workers with their work results by day
     *
     * @param day    Day
     * @param limit  Limit for pagination
     * @param offset Offset for pagination
     * @return Http response with collection of workers with results response model
     */
    @Operation(summary = "Getting all workers with their work results by day")
    @GetMapping("/all")
    public ResponseEntity<Collection<WorkerWithResult>> getDayWorkersWithResultsByDay(
            @RequestParam("day") LocalDate day,
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset
    ) {
        return new ResponseEntity<>(
                statisticService.getWorkersWithResultsByDay(day, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Getting all workers with their work results by day
     *
     * @param periodRequest Request with period date
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Http response with collection of workers with results response model
     */
    @Operation(summary = "Getting all workers with their work results by day")
    @PostMapping("/all")
    public ResponseEntity<Collection<WorkerWithResult>> getPeriodWorkersWithResultsByPeriod(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @Valid
            @RequestBody
            PeriodRequest periodRequest,
            BindingResult bindingResult
    ) {
        return new ResponseEntity<>(
                statisticService.getWorkersWithResultsByPeriod(bindingResult, periodRequest, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Getting all workers by day results
     *
     * @param day    Day
     * @param limit  Limit for pagination
     * @param offset Offset for pagination
     * @return Http response with collection of workers response model
     */
    @Operation(summary = "Getting all workers by day results")
    @GetMapping("/worker")
    public ResponseEntity<Collection<Worker>> getWorkersPerDay(
            @RequestParam("day") LocalDate day,
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset
    ) {
        return new ResponseEntity<>(
                statisticService.getWorkersByDay(day, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Getting all workers by period
     *
     * @param periodRequest Request with period date
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Http response with collection of workers response model
     */
    @Operation(summary = "Getting all workers by period")
    @PostMapping("/worker")
    public ResponseEntity<Collection<Worker>> getWorkersPerPeriod(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @Valid
            @RequestBody
            PeriodRequest periodRequest,
            BindingResult bindingResult
    ) {
        return new ResponseEntity<>(
                statisticService.getWorkersByPeriod(bindingResult, periodRequest, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Getting all products by day results
     *
     * @param day    Day
     * @param limit  Limit for pagination
     * @param offset Offset for pagination
     * @return Http response with collection of products response model
     */
    @Operation(summary = "Getting all products by day results")
    @GetMapping("/product")
    public ResponseEntity<Collection<WorkResult>> getProductsPerDay(
            @RequestParam("day") LocalDate day,
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset
    ) {
        return new ResponseEntity<>(
                statisticService.getProductsByDay(day, limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Getting all products by period
     *
     * @param periodRequest Request with period date
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Http response with collection of products response model
     */
    @Operation(summary = "Getting all products by period")
    @PostMapping("/product")
    public ResponseEntity<Collection<WorkResult>> getProductsPerPeriod(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @Valid
            @RequestBody
            PeriodRequest periodRequest,
            BindingResult bindingResult
    ) {
        return new ResponseEntity<>(
                statisticService.getProductsByPeriod(bindingResult, periodRequest, limit, offset),
                HttpStatus.OK
        );
    }
}
