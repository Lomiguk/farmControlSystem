package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;
import ru.skibin.farmsystem.api.request.product.AddProductRequest;
import ru.skibin.farmsystem.api.request.product.UpdateProductRequest;
import ru.skibin.farmsystem.api.response.ProductResponse;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.repository.ProductDAO;
import ru.skibin.farmsystem.service.mapper.EntityToResponseMapper;
import ru.skibin.farmsystem.service.validation.CommonCheckHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final  Logger LOGGER = Logger.getLogger(ProductService.class.getName());
    private final ProductDAO productDAO;
    private final CommonCheckHelper checkHelper;
    private final EntityToResponseMapper entityMapper;

    /**
     * Adding product to repository
     *
     * @param request Request with new product data
     * @return Product response model
     */
    @Transactional
    public ProductResponse addProduct(BindingResult bindingResult, AddProductRequest request) {
        checkHelper
                .chainCheckValidation(bindingResult)
                .chainCheckProductForExistByName(request.getName());

        Long id = productDAO.addProduct(request.getName(), request.getValueType());
        ProductResponse productResponse = findProduct(id);
        LOGGER.info(String.format("Add new product (%d)", productResponse.getId()));
        return getProduct(id);
    }

    /**
     * Getting product from repository
     *
     * @param id Product numerical identifier
     * @return Product response model
     */
    public ProductResponse getProduct(Long id) {
        ProductResponse productResponse = findProduct(id);
        if (productResponse != null) {
            return productResponse;
        }
        throw new TryToGetNotExistedEntityException(String.format("Trying to get a non-existent product(%d)", id));
    }

    /**
     * Searching product from repository
     *
     * @param id Product numerical identifier
     * @return Product response model or nul
     */
    public ProductResponse findProduct(Long id) {
        ProductEntity productEntity = productDAO.findProduct(id);
        if (productEntity != null) {
            LOGGER.info(String.format("Get product (%d)", id));
            return entityMapper.toResponse(productEntity);
        }
        LOGGER.info(String.format("Product (%d) doesn't exist", id));
        return null;
    }

    /**
     * Searching product from repository by name
     *
     * @param name Product name
     * @return Product response model or nul
     */
    public ProductResponse findProductByName(String name) {
        ProductEntity productEntity = productDAO.findProductByName(name);

        if (productEntity != null) {
            LOGGER.info(String.format("Get product's with name (%s)", name));
            return entityMapper.toResponse(productEntity);
        }
        LOGGER.info(String.format("Product (\"%s\") doesn't exist", name));
        return null;
    }

    /**
     * Searching products with pagination
     *
     * @param limit  Pagination limit
     * @param offset Pagination offset
     * @return Product response model or nul
     */
    @Transactional
    public Collection<ProductResponse> findAllProductsWithPagination(Integer limit, Integer offset) {
        Collection<ProductEntity> productEntities = productDAO.findAllProductsWithPagination(limit, offset);
        LOGGER.info(String.format("Get %d products, with offset: %d", limit, offset));
        Collection<ProductResponse> products = new ArrayList<>();
        for (var productEntity : productEntities) {
            products.add(entityMapper.toResponse(productEntity));
        }

        return products;
    }

    /**
     * Updating product name
     *
     * @param id      Product numerical identifier
     * @param newName New name for product
     * @return Product response model or nul
     */
    @Transactional
    public ProductResponse updateProductName(Long id, String newName) {
        ProductEntity productEntity = checkHelper.checkProductForActive(id);
        if (!productEntity.getName().equals(newName)) {
            productDAO.updateProductName(id, newName);
        }
        LOGGER.info(String.format("Update product (%d) name", id));
        return findProduct(id);
    }

    /**
     * Updating product value Type
     *
     * @param id           Product numerical identifier
     * @param newValueType New value type for product
     * @return Product response model or nul
     */
    @Transactional
    public ProductResponse updateProductValueType(Long id, ValueType newValueType) {
        ProductEntity productEntity = checkHelper.checkProductForActive(id);
        if (!productEntity.getValueType().equals(newValueType)) {
            productDAO.updateProductValueType(id, newValueType);
        }
        LOGGER.info(String.format("Update product (%d) value type to %s", id, newValueType));
        return findProduct(id);
    }

    /**
     * Updating product actuality status
     *
     * @param id        Product numerical identifier
     * @param newStatus New status for product
     * @return Product response model or nul
     */
    @Transactional
    public ProductResponse updateProductActualStatus(Long id, Boolean newStatus) {
        ProductEntity productEntity = checkHelper.checkProductForExist(id);

        if (!productEntity.getIsActual().equals(newStatus)) {
            productDAO.updateActualStatus(id, newStatus);
        }
        LOGGER.info(String.format("Update product (%d) active status to %s", id, newStatus));
        return findProduct(id);
    }

    /**
     * Updating product
     *
     * @param id      Product numerical identifier
     * @param request New data for the product
     * @return Product response model or nul
     */
    @Transactional
    public ProductResponse updateProduct(BindingResult bindingResult, Long id, UpdateProductRequest request) {
        checkHelper
                .chainCheckValidation(bindingResult)
                .checkProductForExist(id);
        productDAO.updateProduct(id, request.getName(), request.getValueType(), request.getIsActual());
        LOGGER.info(String.format("Update product(%d) info", id));
        return findProduct(id);
    }

    /**
     * Deleting or deactivate product
     *
     * @param id product numerical identifier
     * @return true - if successful
     */
    @Transactional
    public Boolean deleteProduct(Long id) {
        if (checkHelper.boolCheckProductInActions(id)) {
            LOGGER.info(String.format("Try to delete product (%d)", id));
            return productDAO.deleteProduct(id) > 0;
        } else {
            LOGGER.info(String.format("product (%d) set actual status to false", id));
            updateProductActualStatus(id, false);
            return true;
        }
    }
}
