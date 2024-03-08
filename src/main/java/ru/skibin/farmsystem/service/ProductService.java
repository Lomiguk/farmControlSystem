package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skibin.farmsystem.api.dto.ProductResponse;
import ru.skibin.farmsystem.api.enumTypes.ValueType;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.exception.common.WrongLimitOffsetException;
import ru.skibin.farmsystem.exception.common.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.exception.common.WrongLongIdValueException;
import ru.skibin.farmsystem.exception.product.WrongProductNameValueException;
import ru.skibin.farmsystem.repository.ProductDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDAO productDAO;
    private final Logger logger = Logger.getLogger(ProductService.class.getName());

    @Transactional
    public ProductResponse addProduct(String name, ValueType valueType) {
        if (name.isBlank() || name.length() < 2 || name.length() > 50) {
            throw new WrongProductNameValueException("Wrong name value (name length must be 2-50 chars)");
        }
        productDAO.addProduct(name, valueType);
        ProductEntity productEntity = productDAO.getProductByName(name);
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
        if (id < 0) { throw new WrongLongIdValueException("Wrong id value"); }

        ProductEntity productEntity = productDAO.findProduct(id);
        if (productEntity != null) {
            logger.info("Get product (" + id + ")");
            return new ProductResponse(productEntity);
        }
        logger.info("Product (" + id + ") doesn't exist");
        return null;
    }

    public ProductResponse findProductByName(String name) {
        if (name.isBlank() || name.length() < 2 || name.length() > 50) {
            throw new WrongProductNameValueException("Wrong name value (name length must be 2-50 chars)");
        }
        ProductEntity productEntity = productDAO.getProductByName(name);
        if (productEntity != null) {
            logger.info("Get product (" + productEntity.getId() + ")");
            return new ProductResponse(productEntity);
        }
        logger.warning("Product (\"" + name + "\") doesn't exist");
        return null;
    }

    public Collection<ProductResponse> findAllProductsWithPagination(Integer limit, Integer offset) {
        if (limit < 0 || offset < 0) throw new WrongLimitOffsetException("Wrong limit/offset values.");

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
        if (id < 0) { throw new WrongLongIdValueException("Wrong id value"); }
        if (newName.isBlank() || newName.length() < 2 || newName.length() > 50) {
            throw new WrongProductNameValueException("Wrong new name value (name length must be 2-50 chars)");
        }
        productDAO.updateProductName(id, newName);
        logger.info("Update product (" + id + ") name");
        return new ProductResponse(productDAO.findProduct(id));
    }

    @Transactional
    public ProductResponse updateProductValueType(Long id, ValueType valueType) {
        if (id < 0) { throw new WrongLongIdValueException("Wrong id value"); }

        productDAO.updateProductValueType(id, valueType);
        logger.info("Update product (" + id + ") value type to " + valueType);
        return new ProductResponse(productDAO.findProduct(id));
    }

    public Boolean deleteProduct(Long id) {
        if (id < 0) { throw new WrongLongIdValueException("Wrong id value"); }

        int result = productDAO.deleteProduct(id);
        logger.info("Delete product (" + id + ")");
        return result == 1;
    }

    @Transactional
    public ProductResponse updateProduct(Long id, String name, ValueType valueType) {
        if (id < 0) { throw new WrongLongIdValueException("Wrong id value"); }
        if (name.isBlank() || name.length() < 2 || name.length() > 50) {
            throw new WrongProductNameValueException("Wrong new name value (name length must be 2-50 chars)");
        }
        productDAO.updateProduct(id, name, valueType);
        logger.info("Update product(" + id + ") info");
        return new ProductResponse(productDAO.findProduct(id));
    }
}
