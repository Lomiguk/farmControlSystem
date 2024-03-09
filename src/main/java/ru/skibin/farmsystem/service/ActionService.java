package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skibin.farmsystem.api.dto.ActionResponse;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.exception.common.WrongProductValueException;
import ru.skibin.farmsystem.repository.ActionDAO;
import ru.skibin.farmsystem.repository.ProductDAO;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ActionService {
    private final ActionDAO actionDAO;
    private final ProductDAO productDAO;

    private final Logger logger = Logger.getLogger(ProductService.class.getName());
    private final CommonCheckHelper checkHelper;

    @Transactional
    public ActionResponse addAction(
            Long profileId,
            Long productId,
            Float value,
            Instant time
    ) {
        ProductEntity product = checkHelper
                .chainCheckProfileForExist(profileId, "Attempt to add an action for non-existed profile")
                .chainCheckTimeForFutureException(time, "Attempt to add an action with the future tense")
                .checkProductForExist(productId, "Attempt to add an action for non-existed product");

        actionDAO.addAction(profileId, productId, value, time);
        ActionEntity actionEntity = actionDAO.findProfileActionByProductAndTime(profileId, productId, time);

        logger.info("Add action (" + actionEntity.getId() + ")");

        return new ActionResponse(actionEntity, product.getValueType());
    }

    @Transactional
    public ActionResponse findAction(Long id) {

        ActionEntity actionEntity = actionDAO.findAction(id);

        if (actionEntity != null) {
            logger.info("Get action (" + id + ")");
            ProductEntity productEntity = productDAO.findProduct(actionEntity.getProductId());
            return new ActionResponse(actionEntity, productEntity.getValueType());
        }
        logger.info("Action (" + id + ") doesn't exist");
        return null;
    }

    @Transactional
    public ActionResponse getAction(Long id) {
        ActionResponse actionResponse = findAction(id);
        if (actionResponse != null) {
            return actionResponse;
        }
        throw new TryToGetNotExistedEntityException("Trying to get a non-existent action(" + id + ")");
    }

    @Transactional
    public Collection<ActionResponse> findPeriodActions(Date start, Date end) {
        return findPeriodActions(start, end, Integer.MAX_VALUE, 0);
    }

    @Transactional
    public Collection<ActionResponse> findPeriodActions(Date start, Date end, Integer limit, Integer offset) {
        checkHelper.chainCheckStartEndOfPeriod(start, end, "Start date biggest end date");
        Collection<ActionResponse> result = new ArrayList<>();

        if (limit == null) {
            result = findPeriodActions(start, end);
        } else {
            if (offset == null) {
                offset = 0;
            }

            Collection<ActionEntity> actionEntities = actionDAO.findPeriodActions(start, end, limit, offset);
            logger.info("Get " + actionEntities.size() + " actions at period (" + start + " - " + end + ")");

            for (var actionEntity : actionEntities) {
                ProductEntity productEntity = productDAO.findProduct(actionEntity.getProductId());
                result.add(new ActionResponse(actionEntity, productEntity.getValueType()));
            }
        }

        return result;
    }

    @Transactional
    public ActionResponse updateActionProfileId(Long id, Long newProfileId) {
        ActionEntity actionEntity = checkHelper
                .chainCheckProfileForExist(newProfileId, "Attempt to add an action for non-existed profile")
                .checkActionForExist(id, "Action doesn't exist");

        actionDAO.updateAction(
                id,
                newProfileId,
                actionEntity.getProductId(),
                actionEntity.getValue(),
                actionEntity.getTime(),
                actionEntity.getIsActual()
        );

        logger.info("Action (" + id + ") was updated (profile id)");
        actionEntity = actionDAO.findAction(id);
        ProductEntity productEntity = productDAO.findProduct(actionEntity.getProductId());
        return new ActionResponse(actionEntity, productEntity.getValueType());
    }

    @Transactional
    public ActionResponse updateActionProductId(Long id, Long newProductId, Float value) {
        ProductEntity productEntity = checkHelper
                .checkProductForExist(newProductId, "Attempt to add an action for non-existed product");

        ActionEntity actionEntity = checkHelper.checkActionForExist(id, "Action doesn't exist");

        Float newValue;
        if (value == null) {
            newValue = actionEntity.getValue();
        } else if (value < 0) {
            throw new WrongProductValueException("Negative value");
        } else {
            newValue = value;
        }

        actionDAO.updateAction(
                id,
                actionEntity.getProfileId(),
                newProductId,
                newValue,
                actionEntity.getTime(),
                actionEntity.getIsActual()
        );
        logger.info("Action (" + id + ") was updated (product id)");
        actionEntity = actionDAO.findAction(id);
        return new ActionResponse(actionEntity, productEntity.getValueType());

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
        logger.info("Action (" + id + ") was updated (actual status)");
        actionEntity = actionDAO.findAction(id);
        ProductEntity productEntity = productDAO.findProduct(actionEntity.getProductId());
        return new ActionResponse(actionEntity, productEntity.getValueType());
    }

    @Transactional
    public ActionResponse updateAction(
            Long id,
            Long newProfileId,
            Long newProductId,
            Float newValue,
            Instant newTime,
            Boolean newActualStatus
    ) {
        ProductEntity productEntity = checkHelper
                .chainCheckProfileForExist(newProfileId, "Attempt to add an action for non-existed profile")
                .chainCheckValueForPositive(newValue, "Negative value")
                .chainCheckTimeForFutureException(newTime, "Attempt to add an action with the future tense")
                .checkProductForExist(newProductId, "Attempt to add an action for non-existed product");

        ActionEntity actionEntity = actionDAO.findAction(id);

        if (newActualStatus == null) newActualStatus = actionEntity.getIsActual();

        actionDAO.updateAction(
                id,
                newProfileId,
                newProductId,
                newValue,
                newTime,
                newActualStatus
        );
        logger.info("Action (" + id + ") was updated");
        actionEntity = actionDAO.findAction(id);
        return new ActionResponse(actionEntity, productEntity.getValueType());
    }

    public Boolean deleteAction(Long id) {
        boolean result = actionDAO.deleteAction(id) > 1;
        if (result) {
            logger.info("Action (" + id + ") was deleted");
        } else {
            logger.info("Action (" + id + ") was deleted");
        }
        return result;
    }
}
