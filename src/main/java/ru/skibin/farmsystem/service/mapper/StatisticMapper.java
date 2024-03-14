package ru.skibin.farmsystem.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.api.response.WorkResultResponse;
import ru.skibin.farmsystem.api.response.WorkerResponse;
import ru.skibin.farmsystem.api.response.WorkerWithResultResponse;
import ru.skibin.farmsystem.entity.WorkResultEntity;
import ru.skibin.farmsystem.entity.StatisticRow;
import ru.skibin.farmsystem.entity.WorkerEntity;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class StatisticMapper {
    private final EntityToResponseMapper entityToResponseMapper;

    public WorkerResponse mapToWorker(StatisticRow statisticRow) {
        return new WorkerResponse(
                statisticRow.getProfileId(),
                statisticRow.getProfileFio(),
                statisticRow.getProfileEmail(),
                statisticRow.getProfileRole()
        );
    }

    public WorkResultResponse mapToProduct(StatisticRow statisticRow) {
        return new WorkResultResponse(
                statisticRow.getProductId(),
                statisticRow.getProductName(),
                statisticRow.getProductValueType(),
                statisticRow.getProductValue()
        );
    }

    public Collection<WorkerWithResultResponse> mapEntityRowCollectionWorkerWithResult(
            Collection<StatisticRow> statisticResult
    ) {
        Collection<WorkerWithResultResponse> result = new ArrayList<>();
        WorkerResponse worker = null;
        WorkerWithResultResponse workerWithResult = null;
        for (var row : statisticResult) {
            if (worker == null || !row.getProfileId().equals(worker.getProfileId())) {
                if (worker != null) {
                    result.add(workerWithResult);
                }
                worker = mapToWorker(row);
                workerWithResult = new WorkerWithResultResponse(worker);
            }
            workerWithResult.getResults().add(mapToProduct(row));
        }
        if (workerWithResult != null) {
            result.add(workerWithResult);
        }
        return result;
    }

    public Collection<WorkerResponse> mapWorkEntityCollectionToWorker(
            Collection<WorkerEntity> workerEntities
    ) {
        Collection<WorkerResponse> result = new ArrayList<>();
        for (var entity : workerEntities) {
            result.add(entityToResponseMapper.toResponse(entity));
        }
        return result;
    }

    public Collection<WorkResultResponse> mapProductEntityCollectionToProductEntity(
            Collection<WorkResultEntity> productDataEntities
    ) {
        Collection<WorkResultResponse> products = new ArrayList<>();
        for (var entities : productDataEntities) {
            products.add(entityToResponseMapper.toResponse(entities));
        }
        return products;
    }
}
