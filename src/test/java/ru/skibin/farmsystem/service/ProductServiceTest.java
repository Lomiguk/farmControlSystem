package ru.skibin.farmsystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skibin.farmsystem.api.dto.ProductResponse;
import ru.skibin.farmsystem.api.enumTypes.ValueType;
import ru.skibin.farmsystem.entity.ProductEntity;
import ru.skibin.farmsystem.exception.TryToGetNotExistedEntityException;
import ru.skibin.farmsystem.exception.WrongLimitOffsetException;
import ru.skibin.farmsystem.exception.WrongLongIdValueException;
import ru.skibin.farmsystem.exception.WrongProductNameValueException;
import ru.skibin.farmsystem.repository.ProductDAO;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductDAO productDAO;

    @InjectMocks
    ProductService productService;

    // util
    protected Random random = new Random();

    @Test
    void handleAddProduct_sendValidData_returnsValidProductResponse() {
        // given
        String name = "test product";
        ValueType valueType = ValueType.LITER;

        ProductEntity productEntity = new ProductEntity(random.nextLong(), name, valueType);
        doReturn(productEntity).when(productDAO).getProductByName(name);

        // when
        ProductResponse productResponse = productService.addProduct(name, valueType);

        //then
        assertNotNull(productResponse);
        assertEquals(productResponse.getName(), name);
        assertEquals(productResponse.getValueType(), valueType);
    }

    @Test
    void handleAddProduct_sendEmptyName_returnsWrongNameException() {
        // given
        boolean isWrongName = false;
        String blankName = "";
        ValueType valueType = ValueType.LITER;

        // when
        try {
            productService.addProduct(blankName, valueType);
        } catch (WrongProductNameValueException e) {
            isWrongName = true;
        }

        //then
        assertTrue(isWrongName);
    }

    @Test
    void handleGetProductById_sendValidData_returnsValidProductResponse() {
        // given
        Long id = Math.abs(random.nextLong());
        ProductEntity productEntity = new ProductEntity(
                id,
                "test product",
                ValueType.LITER
        );

        doReturn(productEntity).when(productDAO).findProduct(id);

        // when
        ProductResponse productResponse = productService.getProduct(id);

        // then
        assertNotNull(productResponse);
        assertEquals(productResponse.getId(), productEntity.getId());
        assertEquals(productResponse.getName(), productEntity.getName());
        assertEquals(productResponse.getValueType(), productEntity.getValueType());
    }

    @Test
    void handleGetProductById_sendNotExistedId_returnsNull() {
        // given
        Long id = Math.abs(random.nextLong());
        boolean isNullResult = false;

        // when
        try {
            productService.getProduct(id);
        } catch (TryToGetNotExistedEntityException e) {
            isNullResult = true;
        }

        //then
        assertTrue(isNullResult);
    }

    @Test
    void handleFindProductById_sendValidData_returnsValidProductResponse() {
        // given
        Long id = Math.abs(random.nextLong());
        ProductEntity productEntity = new ProductEntity(
                id,
                "test product",
                ValueType.LITER
        );

        doReturn(productEntity).when(productDAO).findProduct(id);

        // when
        ProductResponse productResponse = productService.getProduct(id);

        // then
        assertNotNull(productResponse);
        assertEquals(productResponse.getId(), productEntity.getId());
        assertEquals(productResponse.getName(), productEntity.getName());
        assertEquals(productResponse.getValueType(), productEntity.getValueType());
    }

    @Test
    void handleFindProductById_sendNotExistedId_returnsNull() {
        // given
        Long id = Math.abs(random.nextLong());

        // when
        ProductResponse productResponse = productService.findProduct(id);

        // then
        assertNull(productResponse);
    }

    @Test
    void handleFindProductByName_sendValidData_returnsValidResponse() {
        // given
        Long id = Math.abs(random.nextLong());
        ProductEntity productEntity = new ProductEntity(
                id,
                "test product",
                ValueType.LITER
        );
        doReturn(productEntity).when(productDAO).getProductByName(productEntity.getName());

        // when
        ProductResponse productResponse = productService.findProductByName(productEntity.getName());

        //then
        assertNotNull(productResponse);
        assertEquals(productResponse.getId(), productEntity.getId());
        assertEquals(productResponse.getName(), productEntity.getName());
        assertEquals(productResponse.getValueType(), productEntity.getValueType());
    }

    @Test
    void handleFindProductByName_sendBlankName_returnsWrongProductNameException() {
        // given
        boolean isWrongName = false;
        String blankName = "";

        // when
        try {
            productService.findProductByName(blankName);
        } catch (WrongProductNameValueException e) {
            isWrongName = true;
        }
        //then
        assertTrue(isWrongName);
    }

    @Test
    void handleFindAllProductWithPagination_sendValidData_returnsValidProductResponse() {
        // given
        int limit = 10;
        int offset = 0;
        Collection<ProductEntity> productEntities = List.of(
                new ProductEntity(
                        Math.abs(random.nextLong()),
                        "first test product",
                        ValueType.LITER
                ),
                new ProductEntity(
                        Math.abs(random.nextLong()),
                        "second test product",
                        ValueType.LITER
                )
        );

        doReturn(productEntities).when(productDAO).findAllProductsWithPagination(limit, offset);

        // when
        Collection<ProductResponse> productResponses = productService.findAllProductsWithPagination(limit, offset);

        //then
        assertNotNull(productResponses);
        assertEquals(productResponses.size(), productEntities.size());
    }

    @Test
    void handleFindAllProductWithPagination_sendInvalidData_returnsWrongLimitOffsetException() {
        // given
        boolean isWrongLimitOffset = false;
        int limit = -5;
        int offset = -2;


        // when
        try {
            productService.findAllProductsWithPagination(limit, offset);
        } catch (WrongLimitOffsetException e) {
            isWrongLimitOffset = true;
        }
        //then
        assertTrue(isWrongLimitOffset);
    }

    @Test
    void handleUpdateProductName_sendValidData_returnsValidResponse() {
        // given
        Long id = Math.abs(random.nextLong());
        String newName = "new test product Name";
        ValueType valueType = ValueType.LITER;

        ProductEntity productEntity = new ProductEntity(id, newName, valueType);
        doReturn(productEntity).when(productDAO).findProduct(id);

        // when
        ProductResponse productResponse = productService.updateProductName(id, newName);

        //then
        assertNotNull(productResponse);
        assertEquals(productResponse.getName(), newName);
        assertEquals(productResponse.getId(), id);
        assertEquals(productResponse.getValueType(), valueType);
    }
    @Test
    void handleUpdateProductName_sendInvalidId_returnsWrongLongIdValueException() {
        // given
        boolean isWrongId = false;
        Long wrongId = -Math.abs(random.nextLong());
        String newName = "new test product Name";

        // when
        try {
            productService.updateProductName(wrongId, newName);
        } catch (WrongLongIdValueException e) {
            isWrongId = true;
        }
        //then
        assertTrue(isWrongId);
    }

    @Test
    void handleUpdateProductName_sendInvalidName_returnsWrongProductNameValueException() {
        // given
        boolean isWrongName = false;
        Long id = Math.abs(random.nextLong());
        String blankNewName = "";

        // when
        try {
            productService.updateProductName(id, blankNewName);
        } catch (WrongProductNameValueException e) {
            isWrongName = true;
        }
        //then
        assertTrue(isWrongName);
    }

    @Test
    void handleUpdateProductValueType_sendValidData_returnsValidResponse() {
        // given
        Long id = Math.abs(random.nextLong());
        String name = "test product";
        ValueType newValueType = ValueType.LITER;

        ProductEntity productEntity = new ProductEntity(id, name, newValueType);
        doReturn(productEntity).when(productDAO).findProduct(id);

        // when
        ProductResponse productResponse = productService.updateProductValueType(id, newValueType);

        //then
        assertNotNull(productResponse);
        assertEquals(productResponse.getName(), name);
        assertEquals(productResponse.getId(), id);
        assertEquals(productResponse.getValueType(), newValueType);
    }
    @Test
    void handleUpdateProductValueType_sendInvalidId_returnsWrongLongIdValueException() {
        // given
        boolean isWrongId = false;
        Long id = -Math.abs(random.nextLong());
        ValueType newValueType = ValueType.LITER;

        // when
        try {
            productService.updateProductValueType(id, newValueType);
        } catch (WrongLongIdValueException e) {
            isWrongId = true;
        }
        //then
        assertTrue(isWrongId);
    }

    @Test
    void handleDeleteProduct_sendValidData_returnsValidResponse() {
        // given
        Long id = Math.abs(random.nextLong());

        doReturn(1).when(productDAO).deleteProduct(id);

        // when
        boolean isDeleted = productService.deleteProduct(id);

        //then
        assertTrue(isDeleted);
    }
    @Test
    void handleDeleteProduct_sendInvalidId_returnsWrongLongIdValueException() {
        // given
        boolean isWrongId = false;
        final Long id = -Math.abs(random.nextLong());

        // when
        try {
            productService.deleteProduct(id);
        } catch (WrongLongIdValueException e) {
            isWrongId = true;
        }
        //then
        assertTrue(isWrongId);
    }

    @Test
    void handleUpdateProduct_sendValidData_returnsValidResponse() {
        // given
        final Long id = Math.abs(random.nextLong());
        final String newName = "new test product";
        final ValueType newValueType = ValueType.LITER;

        ProductEntity productEntity = new ProductEntity(id, newName, newValueType);
        doReturn(productEntity).when(productDAO).findProduct(id);

        // when
        ProductResponse productResponse = productService.updateProduct(id, newName, newValueType);

        //then
        assertNotNull(productResponse);
        assertEquals(productResponse.getName(), newName);
        assertEquals(productResponse.getId(), id);
        assertEquals(productResponse.getValueType(), newValueType);
    }
    @Test
    void handleUpdateProduct_sendInvalidId_returnsWrongLongIdValueException() {
        // given
        boolean isWrongId = false;
        final Long id = -Math.abs(random.nextLong());
        final ValueType newValueType = ValueType.LITER;

        // when
        try {
            productService.updateProduct(id, "new name", newValueType);
        } catch (WrongLongIdValueException e) {
            isWrongId = true;
        }
        //then
        assertTrue(isWrongId);
    }

    @Test
    void handleUpdateProduct_sendInvalidBlankName_returnsWrongProductNameValueException() {
        // given
        boolean isWrongName = false;
        final Long id = Math.abs(random.nextLong());
        final String blankName = "";
        final ValueType newValueType = ValueType.LITER;

        // when
        try {
            productService.updateProduct(id, blankName, newValueType);
        } catch (WrongProductNameValueException e) {
            isWrongName = true;
        }

        //then
        assertTrue(isWrongName);
    }
}