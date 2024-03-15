package ru.skibin.farmsystem.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ru.skibin.farmsystem.api.data.enumTypes.Role;
import ru.skibin.farmsystem.entity.ActionEntity;
import ru.skibin.farmsystem.entity.MarkEntity;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.entity.ProfileEntity;
import ru.skibin.farmsystem.exception.action.StartEndDateException;
import ru.skibin.farmsystem.exception.common.FutureInstantException;
import ru.skibin.farmsystem.exception.common.NonExistedEntityException;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.exception.common.UniqueConstraintException;
import ru.skibin.farmsystem.exception.common.ValidationException;
import ru.skibin.farmsystem.exception.common.WrongProductValueException;
import ru.skibin.farmsystem.repository.ActionDAO;
import ru.skibin.farmsystem.repository.MarkDAO;
import ru.skibin.farmsystem.repository.ProductDAO;
import ru.skibin.farmsystem.repository.ProfileDAO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class CommonCheckHelper {
    private static final String REQUESTED_RESOURCE_UNACCEPTABLE = "The requested resource cannot be accessed";

    private final ProfileDAO profileDAO;
    private final ProductDAO productDAO;
    private final ActionDAO actionDAO;
    private final MarkDAO markDAO;
    private final AuthorizationCheckHelper authorizationCheckHelper;
    private final Logger logger = Logger.getLogger(CommonCheckHelper.class.getName());

    public ProfileEntity checkProfileForActive(Long id) {
        String message = String.format("Profile %d non-active", id);
        ProfileEntity profileEntity = profileDAO.findProfile(id);
        if (profileEntity == null || !profileEntity.getIsActual()) {
            logger.info(message);
            throw new NonExistedEntityException(message);
        }

        return profileEntity;
    }

    public ProfileEntity checkProfileForExist(Long id) {
        String message = String.format("Non existed profile. id: %d", id);
        ProfileEntity profileEntity = profileDAO.findProfile(id);
        if (profileEntity == null) {
            logger.info(message);
            throw new NonExistedEntityException(message);
        }

        return profileEntity;
    }

    public CommonCheckHelper chainCheckProfileForActive(Long id) {
        checkProfileForActive(id);
        return this;
    }

    public ProductEntity checkProductForActive(Long productId) {
        String message = String.format("Product %d non-active", productId);
        ProductEntity productEntity = productDAO.findProduct(productId);
        if (productEntity == null || !productEntity.getIsActual()) {
            logger.info(message);
            throw new TryToGetNotExistedEntityException(message);
        }
        return productEntity;
    }

    public ProductEntity checkProductForExist(Long productId) {
        String message = String.format("Non-existed product entity. id: %d", productId);
        ProductEntity productEntity = productDAO.findProduct(productId);
        if (productEntity == null) {
            logger.info(message);
            throw new TryToGetNotExistedEntityException(message);
        }
        return productEntity;
    }

    public CommonCheckHelper chainCheckValueForPositive(Float value) {
        String message = "Not positive product value";
        if (value < 0) {
            logger.info(message);
            throw new WrongProductValueException(message);
        }
        return this;
    }

    public CommonCheckHelper chainCheckTimeForFutureException(Instant checkedTime) {
        String message = "Future date - exception";
        if (Instant.now().compareTo(checkedTime) < 0) {
            logger.info(message);
            throw new FutureInstantException(message);
        }

        return this;
    }

    public CommonCheckHelper chainCheckStartEndOfPeriod(LocalDate start, LocalDate end) {
        String message = "Start date later than end date";
        if (start.isAfter(end)) {
            logger.info(message);
            throw new StartEndDateException(message);
        }

        return this;
    }

    public ActionEntity checkActionForActive(Long id) {
        String message = String.format("Action %d is non-active", id);
        ActionEntity actionEntity = actionDAO.findAction(id);
        if (actionEntity == null || !actionEntity.getIsActual()) {
            logger.info(message);
            throw new TryToGetNotExistedEntityException(message);
        }
        return actionEntity;
    }

    public ActionEntity checkActionForExist(Long id) {
        String message = String.format("Action %d is non-existed", id);
        ActionEntity actionEntity = actionDAO.findAction(id);
        if (actionEntity == null) {
            logger.info(message);
            throw new TryToGetNotExistedEntityException(message);
        }
        return actionEntity;
    }

    public Boolean boolCheckProfileInActions(Long id) {
        Collection<ActionEntity> actions = actionDAO.findProfileActions(id, 10, 0);
        if (!actions.isEmpty()) {
            logger.info(String.format("Profile %d is not exist or active", id));
            return false;
        }
        return true;
    }

    public boolean boolCheckProductInActions(Long id) {
        Collection<ActionEntity> actions = actionDAO.findProductActions(id, 10, 0);
        if (!actions.isEmpty()) {
            logger.info(String.format("Action %d is not exist or active", id));
            return false;
        }
        return true;
    }

    public CommonCheckHelper chainCheckForProfileEmailUnique(String email) {
        String message = String.format("Profile with email \"%s\" already exist", email);
        ProfileEntity profileEntity = profileDAO.findProfileByEmail(email);
        if (profileEntity != null) {
            logger.info(message);
            throw new UniqueConstraintException(message);
        }
        return this;
    }

    public CommonCheckHelper chainCheckProductForExistByName(String name) {
        String message = String.format("Profile with name \"%s\" non-exist", name);
        Collection<ProductEntity> productEntities = productDAO.findProductByName(name);
        if (!productEntities.isEmpty()) {
            logger.info(message);
            throw new UniqueConstraintException(message);
        }
        return this;
    }

    public ProfileEntity checkProfileForExistByEmail(String email) {
        String message = String.format("Profile with email \"%s\" non-exist", email);
        ProfileEntity profileEntity = profileDAO.findProfileByEmail(email);
        if (profileEntity == null) {
            logger.info(message);
            throw new TryToGetNotExistedEntityException(message);
        }
        return profileEntity;
    }

    public CommonCheckHelper chainCheckAuthPermission(Long equalId) {
        ProfileEntity profileEntity = authorizationCheckHelper.checkForExistedAuthorizedProfileFromContext();
        if (profileEntity.getRole() != Role.ADMIN) {
            if (!profileEntity.getId().equals(equalId)) {
                logger.info(REQUESTED_RESOURCE_UNACCEPTABLE);
                throw new AccessDeniedException(REQUESTED_RESOURCE_UNACCEPTABLE);
            }
        }
        return this;
    }

    public CommonCheckHelper chainCheckValidation(BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return this;
    }

    public CommonCheckHelper chainCheckActionForExist(Long id) {
        String message = "None-existed action";
        if (actionDAO.findAction(id) == null) {
            logger.info(message);
            throw new NonExistedEntityException(message);
        }
        return this;
    }

    public MarkEntity checkMarkForExist(Long id) {
        String message = "None-existed mark";
        MarkEntity markEntity = markDAO.find(id);
        if (markEntity == null) {
            logger.info(message);
            throw new NonExistedEntityException(message);
        }
        return markEntity;
    }
}
