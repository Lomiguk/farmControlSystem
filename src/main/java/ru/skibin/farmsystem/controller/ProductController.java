package ru.skibin.farmsystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.skibin.farmsystem.api.dto.ProductDTO;
import ru.skibin.farmsystem.api.enumTypes.ValueType;
import ru.skibin.farmsystem.api.request.product.AddProductRequest;
import ru.skibin.farmsystem.api.request.product.UpdateProductRequest;
import ru.skibin.farmsystem.service.ProductService;

import java.util.Collection;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(
            @Valid
            @RequestBody
            AddProductRequest addProductRequest
    ){
        return new ResponseEntity<>(
                productService.addProduct(addProductRequest.getName(), addProductRequest.getValueType()),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(
                productService.getProduct(id),
                HttpStatus.OK
        );
    }

    @GetMapping()
    public ResponseEntity<ProductDTO> getProductByName(
            @NotNull @RequestParam("name") String name
    ) {
        return new ResponseEntity<>(
                productService.getProductByName(name),
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<ProductDTO>> getAll(
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset
    ) {
        return new ResponseEntity<>(
                productService.getAllProductWithPagination(limit, offset),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<ProductDTO> updateProductName(
            @PathVariable("id") Long id,
            @NotNull @RequestParam("new_name") String newName
    ) {
        return new ResponseEntity<>(
                productService.updateProductName(id, newName),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/value-type")
    public ResponseEntity<ProductDTO> updateProductValueType(
            @PathVariable("id") Long id,
            @NotNull @RequestParam("value_type") ValueType valueType
    ) {
        return new ResponseEntity<>(
                productService.updateProductValueType(id, valueType),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateProductRequest updateProductRequest
    ){
        return new ResponseEntity<>(
                productService.updateProduct(id, updateProductRequest.getName(), updateProductRequest.getValueType()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                productService.deleteProduct(id),
                HttpStatus.OK
        );
    }
}
