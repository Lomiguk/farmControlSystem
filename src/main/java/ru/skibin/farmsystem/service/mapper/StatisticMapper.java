package ru.skibin.farmsystem.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.api.request.other.WorkResult;
import ru.skibin.farmsystem.api.request.other.Worker;
import ru.skibin.farmsystem.api.response.WorkerWithResult;
import ru.skibin.farmsystem.entity.WorkResultEntity;
import ru.skibin.farmsystem.entity.StatisticRow;
import ru.skibin.farmsystem.entity.WorkerEntity;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class StatisticMapper {
    private final EntityToResponseMapper entityToResponseMapper;

    public Worker mapToWorker(StatisticRow statisticRow) {
        return new Worker(
                statisticRow.getProfileId(),
                statisticRow.getProfileFio(),
                statisticRow.getProfileEmail(),
                statisticRow.getProfileRole()
        );
    }

    public WorkResult mapToProduct(StatisticRow statisticRow) {
        return new WorkResult(
                statisticRow.getProductId(),
                statisticRow.getProductName(),
                statisticRow.getProductValueType(),
                statisticRow.getProductValue()
        );
    }

    public Collection<WorkerWithResult> mapEntityRowCollectionWorkerWithResult(
            Collection<StatisticRow> statisticResult
    ) {
        Collection<WorkerWithResult> result = new ArrayList<>();
        Worker worker = null;
        WorkerWithResult workerWithResult = null;
        for (var row : statisticResult) {
            if (worker == null || !row.getProfileId().equals(worker.getProfileId())) {
                if (worker != null) {
                    result.add(workerWithResult);
                }
                worker = mapToWorker(row);
                workerWithResult = new WorkerWithResult(worker);
            }
            workerWithResult.getResults().add(mapToProduct(row));
        }
        if (workerWithResult != null) {
            result.add(workerWithResult);
        }
        return result;
    }

    public Collection<Worker> mapWorkEntityCollectionToWorker(
            Collection<WorkerEntity> workerEntities
    ) {
        Collection<Worker> result = new ArrayList<>();
        for (var entity : workerEntities) {
            result.add(entityToResponseMapper.toResponse(entity));
        }
        return result;
    }

    public Collection<WorkResult> mapProductEntityCollectionToProductEntity(
            Collection<WorkResultEntity> productDataEntities
    ) {
        Collection<WorkResult> products = new ArrayList<>();
        for (var entities : productDataEntities) {
            products.add(entityToResponseMapper.toResponse(entities));
        }
        return products;
    }
}
