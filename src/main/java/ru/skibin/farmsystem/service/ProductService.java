package ru.skibin.farmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skibin.farmsystem.api.dto.ProductDTO;
import ru.skibin.farmsystem.api.enumTypes.ValueType;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.exception.LimitOffsetException;
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
    public ProductDTO addProduct(String name, ValueType valueType) {
        productDAO.addProduct(name, valueType);
        ProductEntity productEntity = productDAO.getProductByName(name);
        logger.info("aAd new product (" + productEntity.getId() + ")");
        return new ProductDTO(productEntity);
    }

    public ProductDTO getProduct(Long id) {
        logger.info("Get product (" + id + ")");
        return new ProductDTO(productDAO.getProductById(id));
    }

    public ProductDTO getProductByName(String name) {
        ProductEntity productEntity = productDAO.getProductByName(name);
        logger.info("Get product (" + productEntity.getId() + ")");
        return new ProductDTO(productEntity);
    }

    public Collection<ProductDTO> getAllProductWithPagination(Integer limit, Integer offset) {
        if (limit < 0 || offset < 0) throw new LimitOffsetException("Wrong limit/offset values.");

        Collection<ProductEntity> productEntities = productDAO.getProductAllWithPagination(limit, offset);
        logger.info("Get " + limit + "products, with offset: " + offset);
        Collection<ProductDTO> products = new ArrayList<>();
        for (var productEntity : productEntities) {
            products.add(new ProductDTO(productEntity));
        }

        return products;
    }

    @Transactional
    public ProductDTO updateProductName(Long id, String newName) {
        productDAO.updateProductName(id, newName);
        logger.info("Update product (" + id + ") name");
        return new ProductDTO(productDAO.getProductById(id));
    }

    @Transactional
    public ProductDTO updateProductValueType(Long id, ValueType valueType) {
        productDAO.updateProductValueType(id, valueType);
        logger.info("Update product (" + id + ") value type to " + valueType);
        return new ProductDTO(productDAO.getProductById(id));
    }

    public Boolean deleteProduct(Long id) {
        int result = productDAO.deleteProduct(id);
        logger.info("Delete product (" + id + ")");
        return result == 1;
    }

    public ProductDTO updateProduct(Long id, String name, ValueType valueType) {
        productDAO.updateProduct(id, name, valueType);
        logger.info("Update product(" + id + ") info");
        return new ProductDTO(productDAO.getProductById(id));
    }
}
