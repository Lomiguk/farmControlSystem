package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skibin.farmsystem.api.request.action.PeriodRequest;
import ru.skibin.farmsystem.api.response.ActionResponse;
import ru.skibin.farmsystem.api.response.WorkResultResponse;
import ru.skibin.farmsystem.api.response.WorkerResponse;
import ru.skibin.farmsystem.api.response.WorkerWithResultResponse;
import ru.skibin.farmsystem.entity.StatisticRow;
import ru.skibin.farmsystem.repository.StatisticDAO;
import ru.skibin.farmsystem.service.mapper.StatisticMapper;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticMapper statisticMapper;
    private final ActionService actionService;
    private final StatisticDAO statisticDAO;

    /**
     * Getting all actions by day
     *
     * @param day    Day
     * @param limit  Limit for pagination
     * @param offset Offset for pagination
     * @return Collection of action response models
     */
    public Collection<ActionResponse> getAllActionsByDay(
            LocalDate day,
            Integer limit,
            Integer offset
    ) {
        return actionService.findDayAction(day, limit, offset);
    }

    /**
     * Getting all actions by period
     *
     * @param periodRequest Request with period data
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Collection of action response models
     */
    public Collection<ActionResponse> getAllActionsByPeriod(
            PeriodRequest periodRequest,
            Integer limit,
            Integer offset
    ) {
        return actionService.findPeriodActions(periodRequest, limit, offset);
    }

    /**
     * Getting workers with results by day
     *
     * @param day    Day
     * @param limit  Limit for pagination
     * @param offset Offset for pagination
     * @return Collection of worker with their results response model
     */
    public Collection<WorkerWithResultResponse> getWorkersWithResultsByDay(
            LocalDate day,
            Integer limit,
            Integer offset
    ) {
        if (limit == null) limit = Integer.MAX_VALUE;
        if (offset == null) offset = 0;

        Collection<StatisticRow> statisticResult = statisticDAO.getWorkerWithResultsByDay(day, limit, offset);
        return statisticMapper.mapEntityRowCollectionWorkerWithResult(statisticResult);
    }

    /**
     * Getting workers with results by day
     *
     * @param day    Day
     * @return Collection of worker with their results response model
     */
    public Collection<WorkerWithResultResponse> getWorkersWithResultsByDay(LocalDate day) {
        Collection<StatisticRow> statisticResult = statisticDAO.getWorkerWithResultsByDay(
                day,
                Integer.MAX_VALUE,
                0
        );
        return statisticMapper.mapEntityRowCollectionWorkerWithResult(statisticResult);
    }

    /**
     * Getting workers with results by period
     *
     * @param periodRequest Request with period data
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Collection of worker with their results response model
     */
    public Collection<WorkerWithResultResponse> getWorkersWithResultsByPeriod(
            PeriodRequest periodRequest,
            Integer limit,
            Integer offset
    ) {
        if (limit == null) limit = Integer.MAX_VALUE;
        if (offset == null) offset = 0;

        Collection<StatisticRow> statisticResult = statisticDAO.getWorkerWithResultsByPeriod(
                periodRequest.getStart(),
                periodRequest.getEnd(),
                limit,
                offset
        );
        return statisticMapper.mapEntityRowCollectionWorkerWithResult(statisticResult);
    }

    /**
     * Getting workers by day
     *
     * @param day    Day
     * @param limit  Limit for pagination
     * @param offset Offset for pagination
     * @return Collection of worker response models
     */
    public Collection<WorkerResponse> getWorkersByDay(LocalDate day, Integer limit, Integer offset) {
        if (limit == null) limit = Integer.MAX_VALUE;
        if (offset == null) offset = 0;

        return statisticMapper.mapWorkEntityCollectionToWorker(statisticDAO.getWorkersByDay(day, limit, offset));
    }

    /**
     * Getting workers by period
     *
     * @param periodRequest Request with period data
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Collection of worker response models
     */
    public Collection<WorkerResponse> getWorkersByPeriod(
            PeriodRequest periodRequest,
            Integer limit,
            Integer offset
    ) {
        if (limit == null) limit = Integer.MAX_VALUE;
        if (offset == null) offset = 0;

        return statisticMapper.mapWorkEntityCollectionToWorker(statisticDAO.getWorkersByPeriod(
                periodRequest.getStart(),
                periodRequest.getEnd(),
                limit,
                offset
        ));
    }

    /**
     * Getting work results by day
     *
     * @param day    Day
     * @param limit  Limit for pagination
     * @param offset Offset for pagination
     * @return Collection of work result response models
     */
    public Collection<WorkResultResponse> getProductsByDay(LocalDate day, Integer limit, Integer offset) {
        if (limit == null) limit = Integer.MAX_VALUE;
        if (offset == null) offset = 0;

        return statisticMapper.mapProductEntityCollectionToProductEntity(
                statisticDAO.getProductsByDay(day, limit, offset)
        );
    }

    /**
     * Getting work results by period
     *
     * @param periodRequest Request with period data
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Collection of work result response models
     */
    public Collection<WorkResultResponse> getProductsByPeriod(
            PeriodRequest periodRequest,
            Integer limit,
            Integer offset
    ) {
        if (limit == null) limit = Integer.MAX_VALUE;
        if (offset == null) offset = 0;

        return statisticMapper.mapProductEntityCollectionToProductEntity(
                statisticDAO.getProductsByPeriod(periodRequest.getStart(), periodRequest.getEnd(), limit, offset)
        );
    }


}
