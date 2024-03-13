package ru.skibin.farmsystem.service.mapper;

import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;
import ru.skibin.farmsystem.api.request.other.WorkResult;
import ru.skibin.farmsystem.api.request.other.Worker;
import ru.skibin.farmsystem.api.response.ActionResponse;
import ru.skibin.farmsystem.api.response.ProductResponse;
import ru.skibin.farmsystem.api.response.ProfileResponse;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.entity.WorkResultEntity;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.entity.WorkerEntity;

@Component
public class EntityToResponseMapper {
    public ProfileResponse toResponse(ProfileEntity profileEntity) {
        return new ProfileResponse(
                profileEntity.getId(),
                profileEntity.getFio(),
                profileEntity.getEmail(),
                profileEntity.getRole(),
                profileEntity.getIsActual()
        );
    }

    public ProductResponse toResponse(ProductEntity productEntity) {
        return new ProductResponse(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getValueType(),
                productEntity.getIsActual()
        );
    }

    public ActionResponse toResponse(ActionEntity actionEntity, ValueType valueType) {
        return new ActionResponse(
                actionEntity.getId(),
                actionEntity.getProfileId(),
                actionEntity.getProductId(),
                actionEntity.getValue(),
                valueType,
                actionEntity.getTime(),
                actionEntity.getIsActual()
        );
    }

    public Worker toResponse(WorkerEntity worker) {
        return new Worker(
                worker.getProfileId(),
                worker.getProfileFio(),
                worker.getProfileEmail(),
                worker.getProfileRole()
        );
    }

    public WorkResult toResponse(WorkResultEntity product) {
        return new WorkResult(
                product.getProductId(),
                product.getProductName(),
                product.getProductValueType(),
                product.getProductValue()
        );
    }
}
