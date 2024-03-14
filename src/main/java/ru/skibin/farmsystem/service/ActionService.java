package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.skibin.farmsystem.api.request.action.AddActionRequest;
import ru.skibin.farmsystem.api.request.action.PeriodRequest;
import ru.skibin.farmsystem.api.request.action.UpdateActionRequest;
import ru.skibin.farmsystem.api.response.ActionResponse;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.repository.ActionDAO;
import ru.skibin.farmsystem.repository.ProductDAO;
import ru.skibin.farmsystem.service.mapper.EntityToResponseMapper;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;
import ru.skibin.farmsystem.util.LimitOffsetTransformer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ActionService {
    private final ActionDAO actionDAO;
    private final ProductDAO productDAO;
    private final EntityToResponseMapper entityMapper;

    private final Logger logger = Logger.getLogger(ProductService.class.getName());
    private final CommonCheckHelper checkHelper;
    private final LimitOffsetTransformer limitOffsetTransformer;

    /**
     * Adding action to the repository
     *
     * @param addActionRequest Request with action data
     * @return Action response model
     */
    @Transactional
    public ActionResponse addAction(BindingResult bindingResult, AddActionRequest addActionRequest) {
        checkHelper
                .chainCheckValidation(bindingResult)
                .chainCheckProfileForActive(addActionRequest.getProfileId())
                .chainCheckTimeForFutureException(addActionRequest.getTime())
                .checkProductForActive(addActionRequest.getProductId());

        Long id = actionDAO.addAction(
                addActionRequest.getProfileId(),
                addActionRequest.getProductId(),
                addActionRequest.getValue(),
                addActionRequest.getTime()
        );
        ActionResponse actionResponse = getAction(id);

        logger.info(String.format("Add action (%s)", actionResponse.getId()));

        return actionResponse;
    }

    /**
     * Searching action by id
     *
     * @param id Action numerical identifier
     * @return Action response model
     */
    @Transactional
    public ActionResponse findAction(Long id) {

        ActionEntity actionEntity = actionDAO.findAction(id);

        if (actionEntity != null) {
            logger.info(String.format("Get action (%s)", id));
            ProductEntity productEntity = productDAO.findProduct(actionEntity.getProductId());
            return entityMapper.toResponse(actionEntity, productEntity.getValueType());
        }
        logger.info(String.format("Action (%s) doesn't exist", id));
        return null;
    }

    /**
     * Getting action by id
     *
     * @param id Action numerical identifier
     * @return Action response model
     */
    @Transactional
    public ActionResponse getAction(Long id) {
        ActionResponse actionResponse = findAction(id);
        if (actionResponse != null) {
            return actionResponse;
        }
        throw new TryToGetNotExistedEntityException(String.format("Trying to get a non-existent action(%s)", id));
    }

    /**
     * Getting collection of actions by time period
     *
     * @param request request with time period data
     * @param limit   pagination limit
     * @param offset  pagination offset
     * @return Collection of action response models
     */
    @Transactional
    public Collection<ActionResponse> findPeriodActions(
            PeriodRequest request,
            Integer limit,
            Integer offset
    ) {
        checkHelper.chainCheckStartEndOfPeriod(request.getStart(), request.getEnd());

        limit = limitOffsetTransformer.getLimit(limit);
        offset = limitOffsetTransformer.getOffset(offset);

        Collection<ActionEntity> actionEntities = actionDAO.findPeriodActions(
                request.getStart(),
                request.getEnd(),
                limit,
                offset
        );
        logger.info(String.format(
                "Get %d actions at period (%s - %s)",
                actionEntities.size(),
                request.getStart(),
                request.getEnd()
        ));

        return mapCollectionEntityToResponse(actionEntities);
    }

    private Collection<ActionResponse> mapCollectionEntityToResponse(Collection<ActionEntity> actionEntities) {
        Collection<ActionResponse> result = new ArrayList<>();
        for (var actionEntity : actionEntities) {
            ProductEntity productEntity = productDAO.findProduct(actionEntity.getProductId());
            result.add(entityMapper.toResponse(actionEntity, productEntity.getValueType()));
        }
        return result;
    }

    /**
     * Updating action's profile id
     *
     * @param id           Action's numerical identifier
     * @param newProfileId New profile identifier
     * @return Action response model
     */
    @Transactional
    public ActionResponse updateActionProfileId(Long id, Long newProfileId) {
        ActionEntity actionEntity = checkHelper
                .chainCheckProfileForActive(newProfileId)
                .checkActionForActive(id);

        actionDAO.updateAction(
                id,
                newProfileId,
                actionEntity.getProductId(),
                actionEntity.getValue(),
                actionEntity.getTime(),
                actionEntity.getIsActual()
        );

        logger.info(String.format("Action (%d) was updated (profile id)", id));
        return findAction(id);
    }

    /**
     * Updating action's product id
     *
     * @param id           Action's numerical identifier
     * @param newProductId New product identifier
     * @return Action response model
     */
    @Transactional
    public ActionResponse updateActionProductId(Long id, Long newProductId, Float value) {
        checkHelper.checkProductForActive(newProductId);

        ActionEntity actionEntity = checkHelper.checkActionForActive(id);

        if (value == null) {
            value = actionEntity.getValue();
        }

        actionDAO.updateAction(
                id,
                actionEntity.getProfileId(),
                newProductId,
                value,
                actionEntity.getTime(),
                actionEntity.getIsActual()
        );
        logger.info(String.format("Action (%d) was updated (product id)", id));
        return findAction(id);

    }

    /**
     * Updating action's status of actuality
     *
     * @param id              Action's numerical identifier
     * @param newActualStatus New status of actuality
     * @return Action response model
     */
    @Transactional
    public ActionResponse updateActionActualStatus(Long id, Boolean newActualStatus) {
        ActionEntity actionEntity = checkHelper
                .checkActionForExist(id);

        actionDAO.updateAction(
                id,
                actionEntity.getProfileId(),
                actionEntity.getProductId(),
                actionEntity.getValue(),
                actionEntity.getTime(),
                newActualStatus
        );
        logger.info(String.format("Action (%d) was updated (actual status)", id));
        return findAction(id);
    }

    /**
     * Updating action's status of actuality
     *
     * @param id      Action's numerical identifier
     * @param request Request with action's new data
     * @return Action response model
     */
    @Transactional
    public ActionResponse updateAction(BindingResult bindingResult, Long id, UpdateActionRequest request) {
        checkHelper
                .chainCheckValidation(bindingResult)
                .chainCheckActionForExist(id)
                .chainCheckProfileForActive(request.getProfileId())
                .chainCheckValueForPositive(request.getValue())
                .checkProductForActive(request.getProductId());

        ActionResponse actionResponse = findAction(id);


        Boolean newIsActual = request.getIsActual();
        Instant newTime = request.getTime();
        if (newIsActual == null) {
            newIsActual = actionResponse.isActual();
        }
        if (newTime == null) {
            newTime = actionResponse.getTime();
        }
        checkHelper.chainCheckTimeForFutureException(newTime);
        actionDAO.updateAction(
                id,
                request.getProfileId(),
                request.getProductId(),
                request.getValue(),
                newTime,
                newIsActual
        );
        return findAction(id);
    }

    public Boolean deleteAction(Long id) {
        boolean result = actionDAO.deleteAction(id) > 1;
        if (result) {
            logger.info(String.format("Action (%d) was deleted", id));
        } else {
            logger.info(String.format("Action (%d) wastion't deleted", id));
        }
        return result;
    }

    public Collection<ActionResponse> findDayAction(LocalDate day, Integer limit, Integer offset) {
        limit = limitOffsetTransformer.getLimit(limit);
        offset = limitOffsetTransformer.getOffset(offset);
        Collection<ActionEntity> actions = actionDAO.findDayActions(day, limit, offset);

        return mapCollectionEntityToResponse(actions);
    }

}
