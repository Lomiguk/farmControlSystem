package ru.skibin.farmsystem.service.mapper;

import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;
import ru.skibin.farmsystem.api.request.task.AddTaskRequest;
import ru.skibin.farmsystem.api.request.task.UpdateTaskRequest;
import ru.skibin.farmsystem.api.response.TaskResponse;
import ru.skibin.farmsystem.api.response.WorkResultResponse;
import ru.skibin.farmsystem.api.response.WorkerResponse;
import ru.skibin.farmsystem.api.response.ActionResponse;
import ru.skibin.farmsystem.api.response.MarkResponse;
import ru.skibin.farmsystem.api.response.ProductResponse;
import ru.skibin.farmsystem.api.response.ProfileResponse;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.entity.MarkEntity;
import ru.skibin.farmsystem.entity.TaskEntity;
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

    public WorkerResponse toResponse(WorkerEntity worker) {
        return new WorkerResponse(
                worker.getProfileId(),
                worker.getProfileFio(),
                worker.getProfileEmail(),
                worker.getProfileRole()
        );
    }

    public WorkResultResponse toResponse(WorkResultEntity product) {
        return new WorkResultResponse(
                product.getProductId(),
                product.getProductName(),
                product.getProductValueType(),
                product.getProductValue()
        );
    }

    public MarkResponse toResponse(MarkEntity mark) {
        return new MarkResponse(
                mark.getId(),
                mark.getProfileId(),
                mark.getMark(),
                mark.getDate()
        );
    }

    public TaskResponse toResponse(TaskEntity taskEntity) {
        return new TaskResponse(
                taskEntity.getId(),
                taskEntity.getStartDate(),
                taskEntity.getEndDate(),
                taskEntity.getDescription(),
                taskEntity.getProfileId(),
                taskEntity.getProductId(),
                taskEntity.getValue(),
                taskEntity.getCollectedValue(),
                taskEntity.getIsDone(),
                taskEntity.getIsAborted()
        );
    }

    public TaskEntity fromRequest(UpdateTaskRequest updateTaskRequest) {
        return TaskEntity.builder()
                .startDate(updateTaskRequest.getStartDate())
                .endDate(updateTaskRequest.getEndDate())
                .description(updateTaskRequest.getDescription())
                .profileId(updateTaskRequest.getProfileId())
                .productId(updateTaskRequest.getProductId())
                .profileId(updateTaskRequest.getProfileId())
                .value(updateTaskRequest.getValue())
                .collectedValue(updateTaskRequest.getCollectedValue())
                .isDone(updateTaskRequest.getIsDone())
                .isAborted(updateTaskRequest.getIsAborted())
                .build();
    }
    public TaskEntity fromRequest(AddTaskRequest addTaskRequest) {
        return TaskEntity.builder()
                .startDate(addTaskRequest.getStartDate())
                .endDate(addTaskRequest.getEndDate())
                .description(addTaskRequest.getDescription())
                .profileId(addTaskRequest.getProfileId())
                .productId(addTaskRequest.getProductId())
                .profileId(addTaskRequest.getProfileId())
                .value(addTaskRequest.getValue())
                .build();
    }
}
