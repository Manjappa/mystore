package com.manjappa.store.controllers;

import com.manjappa.store.dtos.ProductDto;
import com.manjappa.store.entities.Category;
import com.manjappa.store.entities.Product;
import com.manjappa.store.mapper.ProductMapper;
import com.manjappa.store.repositories.CategoryRepository;
import com.manjappa.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<ProductDto> getAllProducts(
           /* @RequestHeader(name = "x-auth-token") String authToken, */
            @RequestParam(name = "categoryId", required = false) Byte categoryId) {
        //System.out.println(authToken);
        List<Product> products;
        if(categoryId != null) {
            System.out.println("categoryId: "+ categoryId);
           products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }
        return products.stream().map(productMapper::toProductDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
        Product product= productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toProductDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Category category= categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
           return ResponseEntity.badRequest().build();
        }
        Product product =productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productMapper.toProductDto(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto,
                                                    @PathVariable Long id) {
       Product product = productRepository.findById(id).orElse(null);
       Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            ResponseEntity.notFound().build();
        }
       if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productMapper.update(productDto, product);
       product.setCategory(category);
       productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
           return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
