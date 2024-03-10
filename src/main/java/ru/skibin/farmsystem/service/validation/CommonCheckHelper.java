package ru.skibin.farmsystem.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.exception.action.StartEndDateException;
import ru.skibin.farmsystem.exception.common.FutureInstantException;
import ru.skibin.farmsystem.exception.common.NonExistedProfileException;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.exception.common.WrongProductValueException;
import ru.skibin.farmsystem.repository.ActionDAO;
import ru.skibin.farmsystem.repository.ProductDAO;
import ru.skibin.farmsystem.repository.ProfileDAO;

import java.sql.Date;
import java.time.Instant;
import java.util.Collection;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class CommonCheckHelper {
    private final ProfileDAO profileDAO;
    private final ProductDAO productDAO;
    private final ActionDAO  actionDAO;
    private final Logger logger = Logger.getLogger(CommonCheckHelper.class.getName());

    public ProfileEntity checkProfileForExist(Long id, String exceptionMessage) {
        ProfileEntity profileEntity = profileDAO.findProfile(id);
        if (profileEntity == null) {
            throw new NonExistedProfileException(exceptionMessage);
        }

        return profileEntity;
    }

    public CommonCheckHelper chainCheckProfileForExist(Long id, String exceptionMessage) {
        checkProfileForExist(id, exceptionMessage);

        return this;
    }

    public ProductEntity checkProductForExist(Long productId, String exceptionMessage) {
        ProductEntity product = productDAO.findProduct(productId);
        if (product == null) {
            throw new TryToGetNotExistedEntityException(exceptionMessage);
        }
        return product;
    }

    public CommonCheckHelper chainCheckValueForPositive(Float value, String exceptionMessage) {
        if (value < 0) {
            throw new WrongProductValueException(exceptionMessage);
        }
        return this;
    }

    public CommonCheckHelper chainCheckTimeForFutureException(Instant time, String exceptionMessage) {
        if (Instant.now().compareTo(time) < 0) {
            throw new FutureInstantException(exceptionMessage);
        }

        return this;
    }

    public CommonCheckHelper chainCheckStartEndOfPeriod(Date start, Date end, String exceptionMessage) {
        if (start.compareTo(end) > 0) {
            throw new StartEndDateException(exceptionMessage);
        }

        return this;
    }

    public ActionEntity checkActionForExist(Long id, String exceptionMessage) {
        ActionEntity actionEntity = actionDAO.findAction(id);
        if (actionEntity == null) {
            throw new TryToGetNotExistedEntityException(exceptionMessage);
        }
        return actionEntity;
    }

    public Boolean boolCheckProfileInActions(Long id, String warningMessage) {
        Collection<ActionEntity> actions = actionDAO.findProfileActions(id, 10, 0);
        if (!actions.isEmpty()) {
            logger.warning(warningMessage);
            return false;
        }
        return true;
    }

    public boolean boolCheckProductInActions(Long id, String warningMessage) {
        Collection<ActionEntity> actions = actionDAO.findProductActions(id, 10, 0);
        if (!actions.isEmpty()) {
            logger.warning(warningMessage);
            return false;
        }
        return true;
    }
}
