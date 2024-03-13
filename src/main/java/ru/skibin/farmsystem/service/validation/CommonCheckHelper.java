package ru.skibin.farmsystem.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import ru.skibin.farmsystem.api.enumTypes.Role;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.exception.action.StartEndDateException;
import ru.skibin.farmsystem.exception.common.FutureInstantException;
import ru.skibin.farmsystem.exception.common.NonExistedProfileException;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.exception.common.UniqueConstraintException;
import ru.skibin.farmsystem.exception.common.WrongProductValueException;
import ru.skibin.farmsystem.repository.ActionDAO;
import ru.skibin.farmsystem.repository.ProductDAO;
import ru.skibin.farmsystem.repository.ProfileDAO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class CommonCheckHelper {
    private final String REQUESTED_RESOURCE_UNACCEPTABLE = "The requested resource cannot be accessed";

    private final ProfileDAO profileDAO;
    private final ProductDAO productDAO;
    private final ActionDAO  actionDAO;
    private final AuthorizationCheckHelper authorizationCheckHelper;
    private final Logger logger = Logger.getLogger(CommonCheckHelper.class.getName());

    public ProfileEntity checkProfileForActive(Long id, String exceptionMessage) {
        ProfileEntity profileEntity = profileDAO.findProfile(id);
        if (profileEntity == null || !profileEntity.getIsActual()) {
            throw new NonExistedProfileException(exceptionMessage);
        }

        return profileEntity;
    }

    public ProfileEntity checkProfileForExist(Long id, String exceptionMessage) {
        ProfileEntity profileEntity = profileDAO.findProfile(id);
        if (profileEntity == null) {
            throw new NonExistedProfileException(exceptionMessage);
        }

        return profileEntity;
    }

    public CommonCheckHelper chainCheckProfileForActive(Long id, String exceptionMessage) {
        checkProfileForActive(id, exceptionMessage);

        return this;
    }

    public ProductEntity checkProductForActive(Long productId, String exceptionMessage) {
        ProductEntity productEntity = productDAO.findProduct(productId);
        if (productEntity == null || !productEntity.getIsActual()) {
            throw new TryToGetNotExistedEntityException(exceptionMessage);
        }
        return productEntity;
    }

    public ProductEntity checkProductForExist(Long productId, String exceptionMessage) {
        ProductEntity productEntity = productDAO.findProduct(productId);
        if (productEntity == null) {
            throw new TryToGetNotExistedEntityException(exceptionMessage);
        }
        return productEntity;
    }

    public CommonCheckHelper chainCheckValueForPositive(Float value, String exceptionMessage) {
        if (value < 0) {
            throw new WrongProductValueException(exceptionMessage);
        }
        return this;
    }

    public CommonCheckHelper chainCheckTimeForFutureException(Instant checkedTime, String exceptionMessage) {
        if (Instant.now().compareTo(checkedTime) < 0) {
            throw new FutureInstantException(exceptionMessage);
        }

        return this;
    }

    public CommonCheckHelper chainCheckStartEndOfPeriod(LocalDate start, LocalDate end, String exceptionMessage) {
        if (start.isAfter(end)) {
            throw new StartEndDateException(exceptionMessage);
        }

        return this;
    }

    public ActionEntity checkActionForActive(Long id, String exceptionMessage) {
        ActionEntity actionEntity = actionDAO.findAction(id);
        if (actionEntity == null || !actionEntity.getIsActual()) {
            logger.info(exceptionMessage);
            throw new TryToGetNotExistedEntityException(exceptionMessage);
        }
        return actionEntity;
    }

    public ActionEntity checkActionForExist(Long id, String exceptionMessage) {
        ActionEntity actionEntity = actionDAO.findAction(id);
        if (actionEntity == null) {
            logger.info(exceptionMessage);
            throw new TryToGetNotExistedEntityException(exceptionMessage);
        }
        return actionEntity;
    }

    public Boolean boolCheckProfileInActions(Long id, String message) {
        Collection<ActionEntity> actions = actionDAO.findProfileActions(id, 10, 0);
        if (!actions.isEmpty()) {
            logger.info(message);
            return false;
        }
        return true;
    }

    public boolean boolCheckProductInActions(Long id, String message) {
        Collection<ActionEntity> actions = actionDAO.findProductActions(id, 10, 0);
        if (!actions.isEmpty()) {
            logger.info(message);
            return false;
        }
        return true;
    }

    public CommonCheckHelper chainCheckForProfileEmailUnique(String email, String exceptionMessage) {
        ProfileEntity profileEntity = profileDAO.findProfileByEmail(email);
        if (profileEntity != null) {
            logger.info(exceptionMessage);
            throw new UniqueConstraintException(exceptionMessage);
        }
        return this;
    }

    public ProductEntity checkProductForExistByName(String name, String exceptionMessage) {
        ProductEntity productEntity = productDAO.findProductByName(name);
        if (productEntity != null) {
            logger.info(exceptionMessage);
            throw new UniqueConstraintException(exceptionMessage);
        }
        return productEntity;
    }

    public ProfileEntity checkProfileForExistByEmail(String email, String exceptionMessage) {
        ProfileEntity profileEntity = profileDAO.findProfileByEmail(email);
        if (profileEntity == null) {
            logger.info(exceptionMessage);
            throw new TryToGetNotExistedEntityException(exceptionMessage);
        }
        return profileEntity;
    }

    public CommonCheckHelper checkAuthPermission(Long equalId) {
        ProfileEntity profileEntity = authorizationCheckHelper.checkForExistedAuthorizedProfileFromContext();
        if (profileEntity.getRole() != Role.ADMIN) {
            if (!profileEntity.getId().equals(equalId)) {
                logger.info(REQUESTED_RESOURCE_UNACCEPTABLE);
                throw new AccessDeniedException(REQUESTED_RESOURCE_UNACCEPTABLE);
            }
        }
        return this;
    }
}
