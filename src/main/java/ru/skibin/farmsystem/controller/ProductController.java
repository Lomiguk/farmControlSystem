package ru.skibin.farmsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skibin.farmsystem.api.data.enumTypes.ValueType;
import ru.skibin.farmsystem.api.request.product.AddProductRequest;
import ru.skibin.farmsystem.api.request.product.UpdateProductRequest;
import ru.skibin.farmsystem.api.response.ProductResponse;
import ru.skibin.farmsystem.exception.common.ValidationException;
import ru.skibin.farmsystem.service.ProductService;

import java.util.Collection;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * Adding product to repository
     * @param addProductRequest Request with new product data
     * @param bindingResult     Request validation data
     * @return Http response with new product response model
     */
    @Operation(summary = "Adding product to repository")
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(
            @Valid @RequestBody
            AddProductRequest addProductRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return new ResponseEntity<>(
                productService.addProduct(addProductRequest),
                HttpStatus.OK
        );
    }

    /**
     * Getting product from repository
     * @param id Product numerical identifier
     * @return Http response with product response model
     */
    @Operation(summary = "Getting product from repository")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable("id")
            @Validated
            @Positive
            Long id
    ) {
        return new ResponseEntity<>(
                productService.getProduct(id),
                HttpStatus.OK
        );
    }

    /**
     * Getting product from repository by name
     * @param name Product name
     * @return Http response with product response model
     */
    @Operation(summary = "Getting product from repository by name")
    @GetMapping()
    public ResponseEntity<ProductResponse> getProductByName(
            @Validated
            @NotNull(message = "product name can't be null")
            @NotEmpty(message = "product name can't be empty")
            @Size(min = 2, max = 50, message = "product name: 2-50 chars")
            @RequestParam("name")
            String name
    ) {
        return new ResponseEntity<>(
                productService.findProductByName(name),
                HttpStatus.OK
        );
    }

    /**
     * Getting products from repository with pagination
     * @param limit  Pagination limit
     * @param offset Pagination offset
     * @return Http response with collection of product response models
     */
    @Operation(summary = "Getting products from repository with pagination")
    @GetMapping("/all")
    public ResponseEntity<Collection<ProductResponse>> getAll(
            @Validated
            @PositiveOrZero(message = "limit must be positive")
            @RequestParam("limit")
            Integer limit,
            @Validated
            @PositiveOrZero(message = "offset must be positive")
            @RequestParam("offset")
            Integer offset
    ) {
        return new ResponseEntity<>(
                productService.findAllProductsWithPagination(limit, offset),
                HttpStatus.OK
        );
    }

    /**
     * Updating product name
     * @param id      Product numerical identifier
     * @param newName New product name
     * @return Http response with product response model
     */
    @Operation(summary = "Updating product name")
    @PatchMapping("/{id}/name")
    public ResponseEntity<ProductResponse> updateProductName(
            @PathVariable("id") Long id,
            @RequestParam("new_name") String newName
    ) {
        return new ResponseEntity<>(
                productService.updateProductName(id, newName),
                HttpStatus.OK
        );
    }

    /**
     * Updating product valueType
     * @param id        Product numerical identifier
     * @param valueType New product name
     * @return Http response with product response model
     */
    @Operation(summary = "Updating product valueType")
    @PatchMapping("/{id}/value-type")
    public ResponseEntity<ProductResponse> updateProductValueType(
            @PathVariable("id")
            @Validated
            @Positive
            Long id,
            @RequestParam("value") ValueType valueType
    ) {
        return new ResponseEntity<>(
                productService.updateProductValueType(id, valueType),
                HttpStatus.OK
        );
    }

    /**
     * Updating product status of actuality
     * @param id        Product numerical identifier
     * @param status    New product actuality status
     * @return Http response with product response model
     */
    @Operation(summary = "Updating product status of actuality")
    @PatchMapping("/{id}/actual-status")
    public ResponseEntity<ProductResponse> updateProductActualStatus(
            @PathVariable("id")
            @Validated
            @Positive
            Long id,
            @RequestParam("value") Boolean status
    ) {
        return new ResponseEntity<>(
                productService.updateProductActualStatus(id, status),
                HttpStatus.OK
        );
    }

    /**
     * Updating product
     * @param id                      Product numerical identifier
     * @param updateProductRequest    Request with new data of the product
     * @return Http response with product response model
     */
    @Operation(summary = "Updating product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id")
            @Validated
            @Positive
            Long id,
            @Valid @RequestBody UpdateProductRequest updateProductRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        return new ResponseEntity<>(
                productService.updateProduct(id, updateProductRequest),
                HttpStatus.OK
        );
    }

    /**
     * Deleting or deactivate product
     * @param id product numerical idetifier
     * @return Http response with product response model
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(
            @PathVariable("id")
            @Validated
            @Positive
            Long id
    ) {
        return new ResponseEntity<>(
                productService.deleteProduct(id),
                HttpStatus.OK
        );
    }
}
