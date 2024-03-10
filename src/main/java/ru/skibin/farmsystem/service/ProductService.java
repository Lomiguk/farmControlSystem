package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skibin.farmsystem.api.dto.ProductResponse;
import ru.skibin.farmsystem.api.enumTypes.ValueType;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.repository.ProductDAO;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDAO productDAO;
    private final Logger logger = Logger.getLogger(ProductService.class.getName());
    private final CommonCheckHelper checkHelper;

    @Transactional
    public ProductResponse addProduct(String name, ValueType valueType) {
        checkHelper.checkProductForExistByName(name, "Product with that name already exist");

        productDAO.addProduct(name, valueType);
        ProductEntity productEntity = productDAO.findProductByName(name);
        logger.info("Add new product (" + productEntity.getId() + ")");
        return new ProductResponse(productEntity);
    }

    public ProductResponse getProduct(Long id) {
        ProductResponse productResponse = findProduct(id);
        if (productResponse != null) {
            return productResponse;
        }
        throw new TryToGetNotExistedEntityException("Trying to get a non-existent product("+id+")");
    }

    public ProductResponse findProduct(Long id) {
        ProductEntity productEntity = productDAO.findProduct(id);
        if (productEntity != null) {
            logger.info("Get product (" + id + ")");
            return new ProductResponse(productEntity);
        }
        logger.info("Product (" + id + ") doesn't exist");
        return null;
    }

    public ProductResponse findProductByName(String name) {
        ProductEntity productEntity = productDAO.findProductByName(name);
        if (productEntity != null) {
            logger.info("Get product (" + productEntity.getId() + ")");
            return new ProductResponse(productEntity);
        }
        logger.warning("Product (\"" + name + "\") doesn't exist");
        return null;
    }

    @Transactional
    public Collection<ProductResponse> findAllProductsWithPagination(Integer limit, Integer offset) {
        Collection<ProductEntity> productEntities = productDAO.findAllProductsWithPagination(limit, offset);
        logger.info("Get " + limit + "products, with offset: " + offset);
        Collection<ProductResponse> products = new ArrayList<>();
        for (var productEntity : productEntities) {
            products.add(new ProductResponse(productEntity));
        }

        return products;
    }

    @Transactional
    public ProductResponse updateProductName(Long id, String newName) {
        ProductEntity productEntity = checkHelper.checkProductForActive(id, "Non existed product can't be update");
        if (!productEntity.getName().equals(newName)) {
            productDAO.updateProductName(id, newName);
        }
        logger.info("Update product (" + id + ") name");
        return new ProductResponse(productDAO.findProduct(id));
    }

    @Transactional
    public ProductResponse updateProductValueType(Long id, ValueType newValueType) {
        ProductEntity productEntity = checkHelper.checkProductForActive(id, "Non existed product can't be update");
        if (!productEntity.getValueType().equals(newValueType)) {
            productDAO.updateProductValueType(id, newValueType);
        }
        logger.info("Update product (" + id + ") value type to " + newValueType);
        return new ProductResponse(productDAO.findProduct(id));
    }

    @Transactional
    public ProductResponse updateProductActualStatus(Long id, Boolean newStatus) {
        ProductEntity productEntity = checkHelper.checkProductForExist(id, "Non existed product can't be update");

        if(!productEntity.getIsActual().equals(newStatus)) {
            productDAO.updateActualStatus(id, newStatus);
        }
        logger.info("Update product (" + id + ") active status to " + newStatus);
        return new ProductResponse(productDAO.findProduct(id));
    }

    @Transactional
    public Boolean deleteProduct(Long id) {
        checkHelper.checkProfileForActive(id, "Non-existed product for delete.");
        if (checkHelper.boolCheckProductInActions(id, "Non-deletable product (has dependent actions)")) {
            logger.info("Try to delete product (" + id + ")");
            return productDAO.deleteProduct(id) > 0;
        } else {
            logger.info("product (" + id + ") set actual status to false");
            updateProductActualStatus(id, false);
            return true;
        }
    }

    @Transactional
    public ProductResponse updateProduct(Long id, String name, ValueType valueType, Boolean isActual) {
        checkHelper.checkProductForExist(id, "Non existed product can't be update");
        productDAO.updateProduct(id, name, valueType, isActual);
        logger.info("Update product(" + id + ") info");
        return new ProductResponse(productDAO.findProduct(id));
    }
}
