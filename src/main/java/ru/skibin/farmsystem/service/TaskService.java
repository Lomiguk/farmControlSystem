package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.skibin.farmsystem.api.request.task.AddTaskRequest;
import ru.skibin.farmsystem.api.request.task.UpdateTaskRequest;
import ru.skibin.farmsystem.api.response.TaskResponse;
import ru.skibin.farmsystem.entity.TaskEntity;
import ru.skibin.farmsystem.exception.common.NonExistedEntityException;
import ru.skibin.farmsystem.repository.TaskDAO;
import ru.skibin.farmsystem.service.mapper.EntityToResponseMapper;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskDAO taskDAO;
    private final CommonCheckHelper commonCheckHelper;
    private final EntityToResponseMapper entityMapper;

    @Transactional
    public TaskResponse add(BindingResult bindingResult, AddTaskRequest addTaskRequest) {
        commonCheckHelper
                .chainCheckValidation(bindingResult)
                .chainCheckProductForActive(addTaskRequest.getProductId())
                .chainCheckProfileForActive(addTaskRequest.getProfileId())
                .chainCheckStartEndOfPeriod(addTaskRequest.getStartDate(), addTaskRequest.getEndDate());
        TaskEntity taskEntity = entityMapper.fromRequest(addTaskRequest);

        Long id = taskDAO.add(taskEntity);

        return get(id);
    }

    @Transactional
    public TaskResponse get(Long id) {
        TaskEntity taskEntity;
        try {
            taskEntity = taskDAO.get(id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NonExistedEntityException(String.format("Non existed task: %s", e));
        }
        return entityMapper.toResponse(taskEntity);
    }

    @Transactional
    public Collection<TaskResponse> getProfilesTasks(Long profileId) {
        Collection<TaskEntity> entities = taskDAO.getByProfile(profileId);
        Collection<TaskResponse> taskResponses = new ArrayList<>();
        for (var entity : entities) {
            taskResponses.add(entityMapper.toResponse(entity));
        }
        return taskResponses;
    }

    @Transactional
    public TaskResponse update(BindingResult bindingResult, Long id, UpdateTaskRequest updateTaskRequest) {
        commonCheckHelper
                .chainCheckValidation(bindingResult)
                .chainCheckTaskForExist(id)
                .chainCheckProductForActive(updateTaskRequest.getProductId())
                .chainCheckProfileForActive(updateTaskRequest.getProfileId())
                .chainCheckStartEndOfPeriod(updateTaskRequest.getStartDate(), updateTaskRequest.getEndDate());
        TaskEntity newTaskData = entityMapper.fromRequest(updateTaskRequest);
        taskDAO.update(id, newTaskData);

        return get(id);
    }

    @Transactional
    public TaskResponse updateCollectedValue(Long id, Float additionalValue) {
        commonCheckHelper
                .chainCheckTaskForExist(id);
        TaskResponse taskResponse = get(id);

        Float value = taskResponse.getCollectedValue() + additionalValue;
        taskDAO.updateValue(id, value, taskResponse.getValue() <= value);

        return get(id);
    }

    public Boolean delete(Long id) {
        return taskDAO.delete(id) > 0;
    }
}
