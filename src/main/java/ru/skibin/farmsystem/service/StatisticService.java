package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.skibin.farmsystem.api.request.other.WorkResult;
import ru.skibin.farmsystem.api.request.other.Worker;
import ru.skibin.farmsystem.api.request.action.PeriodRequest;
import ru.skibin.farmsystem.api.response.ActionResponse;
import ru.skibin.farmsystem.api.response.WorkerWithResult;
import ru.skibin.farmsystem.entity.StatisticRow;
import ru.skibin.farmsystem.repository.StatisticDAO;
import ru.skibin.farmsystem.service.mapper.StatisticMapper;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticMapper statisticMapper;
    private final ActionService actionService;
    private final StatisticDAO statisticDAO;
    private final CommonCheckHelper commonCheckHelper;

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
     * @param bindingResult Request validation data
     * @param periodRequest Request with period data
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Collection of action response models
     */
    public Collection<ActionResponse> getAllActionsByPeriod(
            BindingResult bindingResult,
            PeriodRequest periodRequest,
            Integer limit,
            Integer offset
    ) {
        commonCheckHelper.chainCheckValidation(bindingResult);
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
    public Collection<WorkerWithResult> getWorkersWithResultsByDay(
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
     * Getting workers with results by period
     *
     * @param bindingResult Request validation data
     * @param periodRequest Request with period data
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Collection of worker with their results response model
     */
    public Collection<WorkerWithResult> getWorkersWithResultsByPeriod(
            BindingResult bindingResult,
            PeriodRequest periodRequest,
            Integer limit,
            Integer offset
    ) {
        commonCheckHelper.chainCheckValidation(bindingResult);

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
    public Collection<Worker> getWorkersByDay(LocalDate day, Integer limit, Integer offset) {
        if (limit == null) limit = Integer.MAX_VALUE;
        if (offset == null) offset = 0;

        return statisticMapper.mapWorkEntityCollectionToWorker(statisticDAO.getWorkersByDay(day, limit, offset));
    }

    /**
     * Getting workers by period
     *
     * @param bindingResult Request validation data
     * @param periodRequest Request with period data
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Collection of worker response models
     */
    public Collection<Worker> getWorkersByPeriod(
            BindingResult bindingResult,
            PeriodRequest periodRequest,
            Integer limit,
            Integer offset
    ) {
        commonCheckHelper.chainCheckValidation(bindingResult);

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
    public Collection<WorkResult> getProductsByDay(LocalDate day, Integer limit, Integer offset) {
        if (limit == null) limit = Integer.MAX_VALUE;
        if (offset == null) offset = 0;

        return statisticMapper.mapProductEntityCollectionToProductEntity(
                statisticDAO.getProductsByDay(day, limit, offset)
        );
    }

    /**
     * Getting work results by period
     *
     * @param bindingResult Request validation data
     * @param periodRequest Request with period data
     * @param limit         Limit for pagination
     * @param offset        Offset for pagination
     * @return Collection of work result response models
     */
    public Collection<WorkResult> getProductsByPeriod(
            BindingResult bindingResult,
            PeriodRequest periodRequest,
            Integer limit,
            Integer offset
    ) {
        commonCheckHelper.chainCheckValidation(bindingResult);

        if (limit == null) limit = Integer.MAX_VALUE;
        if (offset == null) offset = 0;

        return statisticMapper.mapProductEntityCollectionToProductEntity(
                statisticDAO.getProductsByPeriod(periodRequest.getStart(), periodRequest.getEnd(), limit, offset)
        );
    }


}
