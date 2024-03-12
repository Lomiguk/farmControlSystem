package ru.skibin.farmsystem.service.mapper;

import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.api.enumTypes.ValueType;
import ru.skibin.farmsystem.api.response.ActionResponse;
import ru.skibin.farmsystem.api.response.ProductResponse;
import ru.skibin.farmsystem.api.response.ProfileResponse;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.entity.ProfileEntity;

@Component
public class EntityToResponseMapper {
    public ProfileResponse profileToResponse(ProfileEntity profileEntity) {
        return new ProfileResponse(
                profileEntity.getId(),
                profileEntity.getFio(),
                profileEntity.getEmail(),
                profileEntity.getRole(),
                profileEntity.getIsActual()
        );
    }

    public ProductResponse productToResponse(ProductEntity productEntity) {
        return new ProductResponse(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getValueType(),
                productEntity.getIsActual()
        );
    }

    public ActionResponse actionMapper(ActionEntity actionEntity, ValueType valueType) {
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
}
