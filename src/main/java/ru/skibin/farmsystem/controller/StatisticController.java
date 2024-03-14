package ru.skibin.farmsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skibin.farmsystem.api.request.action.PeriodRequest;
import ru.skibin.farmsystem.api.response.ActionResponse;
import ru.skibin.farmsystem.api.response.WorkResultResponse;
import ru.skibin.farmsystem.api.response.WorkerResponse;
import ru.skibin.farmsystem.api.response.WorkerWithResultResponse;
import ru.skibin.farmsystem.service.StatisticService;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
@Tag(name = "Statistic")
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
    @GetMapping("/action/day")
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
    @GetMapping("/action/period")
    public ResponseEntity<Collection<ActionResponse>> getAllActionsByPeriod(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @Validated PeriodRequest periodRequest
    ) {
        return new ResponseEntity<>(
                statisticService.getAllActionsByPeriod(periodRequest, limit, offset),
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
    @GetMapping("/all/day")
    public ResponseEntity<Collection<WorkerWithResultResponse>> getDayWorkersWithResultsByDay(
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
    @GetMapping("/all/period")
    public ResponseEntity<Collection<WorkerWithResultResponse>> getPeriodWorkersWithResultsByPeriod(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @Validated PeriodRequest periodRequest
    ) {
        return new ResponseEntity<>(
                statisticService.getWorkersWithResultsByPeriod(periodRequest, limit, offset),
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
    @GetMapping("/worker/day")
    public ResponseEntity<Collection<WorkerResponse>> getWorkersPerDay(
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
    @GetMapping("/worker/period")
    public ResponseEntity<Collection<WorkerResponse>> getWorkersPerPeriod(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @Validated PeriodRequest periodRequest
    ) {
        return new ResponseEntity<>(
                statisticService.getWorkersByPeriod(periodRequest, limit, offset),
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
    @GetMapping("/product/day")
    public ResponseEntity<Collection<WorkResultResponse>> getProductsPerDay(
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
    @GetMapping("/product/period")
    public ResponseEntity<Collection<WorkResultResponse>> getProductsPerPeriod(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @Validated PeriodRequest periodRequest
    ) {
        return new ResponseEntity<>(
                statisticService.getProductsByPeriod(periodRequest, limit, offset),
                HttpStatus.OK
        );
    }
}
