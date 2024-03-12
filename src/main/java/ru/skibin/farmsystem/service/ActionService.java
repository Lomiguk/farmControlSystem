package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skibin.farmsystem.api.request.action.AddActionRequest;
import ru.skibin.farmsystem.api.request.action.GetAllActionsForPeriodRequest;
import ru.skibin.farmsystem.api.request.action.UpdateActionRequest;
import ru.skibin.farmsystem.api.response.ActionResponse;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.exception.common.WrongProductValueException;
import ru.skibin.farmsystem.repository.ActionDAO;
import ru.skibin.farmsystem.repository.ProductDAO;
import ru.skibin.farmsystem.service.mapper.EntityToResponseMapper;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;

import java.time.Instant;
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

    @Transactional
    public ActionResponse addAction(AddActionRequest addActionRequest) {
        ProductEntity product = checkHelper
                .chainCheckProfileForActive(
                        addActionRequest.getProfileId(),
                        "Attempt to add an action for non-existed profile"
                )
                .chainCheckTimeForFutureException(
                        addActionRequest.getTime(),
                        "Attempt to add an action with the future tense"
                )
                .checkProductForActive(
                        addActionRequest.getProductId(),
                        "Attempt to add an action for non-existed product"
                );

        Long id = actionDAO.addAction(
                addActionRequest.getProfileId(),
                addActionRequest.getProductId(),
                addActionRequest.getValue(),
                addActionRequest.getTime()
        );
        ActionEntity actionEntity = actionDAO.findProfileActionByProductAndTime(
                addActionRequest.getProfileId(),
                addActionRequest.getProductId(),
                addActionRequest.getTime()
        );

        logger.info(String.format("Add action (%s)", actionEntity.getId()));

        return entityMapper.actionMapper(actionEntity, product.getValueType());
    }

    @Transactional
    public ActionResponse findAction(Long id) {

        ActionEntity actionEntity = actionDAO.findAction(id);

        if (actionEntity != null) {
            logger.info(String.format("Get action (%s)", id));
            ProductEntity productEntity = productDAO.findProduct(actionEntity.getProductId());
            return entityMapper.actionMapper(actionEntity, productEntity.getValueType());
        }
        logger.info(String.format("Action (%s) doesn't exist", id));
        return null;
    }

    @Transactional
    public ActionResponse getAction(Long id) {
        ActionResponse actionResponse = findAction(id);
        if (actionResponse != null) {
            return actionResponse;
        }
        throw new TryToGetNotExistedEntityException(String.format("Trying to get a non-existent action(%s)", id));
    }

    @Transactional
    public Collection<ActionResponse> findPeriodActions(
            GetAllActionsForPeriodRequest request,
            Integer limit,
            Integer offset
    ) {
        checkHelper.chainCheckStartEndOfPeriod(
                request.getStart(),
                request.getEnd(),
                "Start date biggest end date"
        );
        Collection<ActionResponse> result = new ArrayList<>();

        if (limit == null) {
            limit = Integer.MAX_VALUE;
        }
        if (offset == null) {
            offset = 0;
        }

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

        for (var actionEntity : actionEntities) {
            ProductEntity productEntity = productDAO.findProduct(actionEntity.getProductId());
            result.add(entityMapper.actionMapper(actionEntity, productEntity.getValueType()));
        }

        return result;
    }

    @Transactional
    public ActionResponse updateActionProfileId(Long id, Long newProfileId) {
        ActionEntity actionEntity = checkHelper
                .chainCheckProfileForActive(newProfileId, "Attempt to add an action for non-existed profile")
                .checkActionForActive(id, "Action doesn't exist");

        actionDAO.updateAction(
                id,
                newProfileId,
                actionEntity.getProductId(),
                actionEntity.getValue(),
                actionEntity.getTime(),
                actionEntity.getIsActual()
        );

        logger.info(String.format("Action (%d) was updated (profile id)", id));
        actionEntity = actionDAO.findAction(id);
        ProductEntity productEntity = productDAO.findProduct(actionEntity.getProductId());
        return entityMapper.actionMapper(actionEntity, productEntity.getValueType());
    }

    @Transactional
    public ActionResponse updateActionProductId(Long id, Long newProductId, Float value) {
        ProductEntity productEntity = checkHelper
                .checkProductForActive(newProductId, "Attempt to add an action for non-existed product");

        ActionEntity actionEntity = checkHelper.checkActionForActive(id, "Action doesn't exist");

        if (value == null) {
            value = actionEntity.getValue();
        } else if (value < 0) {
            throw new WrongProductValueException("Negative value");
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
        actionEntity = actionDAO.findAction(id);
        return entityMapper.actionMapper(actionEntity, productEntity.getValueType());

    }

    @Transactional
    public ActionResponse updateActionActualStatus(Long id, Boolean newActualStatus) {
        ActionEntity actionEntity = checkHelper
                .checkActionForExist(id, "Action doesn't exist");

        actionDAO.updateAction(
                id,
                actionEntity.getProfileId(),
                actionEntity.getProductId(),
                actionEntity.getValue(),
                actionEntity.getTime(),
                newActualStatus
        );
        logger.info(String.format("Action (%d) was updated (actual status)", id));
        actionEntity = actionDAO.findAction(id);
        ProductEntity productEntity = productDAO.findProduct(actionEntity.getProductId());
        return entityMapper.actionMapper(actionEntity, productEntity.getValueType());
    }

    @Transactional
    public ActionResponse updateAction(Long id, UpdateActionRequest request) {
        ProductEntity productEntity = checkHelper
                .chainCheckProfileForActive(
                        request.getProfileId(),
                        "Attempt to add an action for non-existed profile"
                )
                .chainCheckValueForPositive(request.getValue(), "Negative value")
                .checkProductForActive(
                        request.getProductId(),
                        "Attempt to add an action for non-existed product"
                );

        ActionEntity actionEntity = actionDAO.findAction(id);

        Boolean newIsActual = request.getIsActual();
        Instant newTime = request.getTime();
        if (newIsActual == null) {
            newIsActual = actionEntity.getIsActual();
        }
        if (newTime == null) {
            newTime = actionEntity.getTime();
        } else {
            checkHelper.chainCheckTimeForFutureException(newTime, "Attempt to add an action with the future tense");
        }

        actionDAO.updateAction(
                id,
                request.getProfileId(),
                request.getProductId(),
                request.getValue(),
                newTime,
                newIsActual
        );
        logger.info(String.format("Action (%d) was updated", id));
        actionEntity = actionDAO.findAction(id);
        return entityMapper.actionMapper(actionEntity, productEntity.getValueType());
    }

    public Boolean deleteAction(Long id) {
        boolean result = actionDAO.deleteAction(id) > 1;
        if (result) {
            logger.info(String.format("Action (%d) was deleted", id));
        } else {
            logger.info(String.format("Action (%d) wasn't deleted", id));
        }
        return result;
    }
}
