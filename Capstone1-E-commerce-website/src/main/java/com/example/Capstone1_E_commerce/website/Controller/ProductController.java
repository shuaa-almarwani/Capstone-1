package com.example.Capstone1_E_commerce.website.Controller;

import com.example.Capstone1_E_commerce.website.Api.ApiResponse;
import com.example.Capstone1_E_commerce.website.Model.Product;
import com.example.Capstone1_E_commerce.website.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<ArrayList<Product>> getProducts (){
        return ResponseEntity.status(200).body(productService.getProducts());
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody @Valid Product product, Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        boolean result = productService.addProduct(product);

        if(result){
            return ResponseEntity.status(201)
                    .body(new ApiResponse("Product added successfully"));
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse("Invalid category ID or duplicate product ID"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable String id,
                                                     @RequestBody @Valid Product product,
                                                     Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        if(productService.updateProduct(id, product)){
            return ResponseEntity.ok(
                    new ApiResponse("Product updated successfully")
            );
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse("Invalid category or product not found"));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct (@PathVariable String id){

        if(productService.deleteProduct(id)){
            return ResponseEntity.status(200).body(new ApiResponse("product deleted successfully"));

        }
        return ResponseEntity.status(404).body(new ApiResponse("product with id "+id+" not found"));
    }


    @GetMapping("/products/sort/{sortType}")
    public ResponseEntity<?> getProductBasedPrice(
            @PathVariable String sortType){

        if(!sortType.equals("asc") &&
                !sortType.equals("desc")) {

            return ResponseEntity.status(400)
                    .body(new ApiResponse(
                            "sortType must be asc or desc"));
        }

        ArrayList<Product> result =
                productService.getProductBasedPrice(sortType);

        if(result.isEmpty()){
            return ResponseEntity.status(404)
                    .body(new ApiResponse("products not found"));
        }

        return ResponseEntity.status(200).body(result);
    }
    @GetMapping("/get-product-in-range/{min}/{max}")
    public ResponseEntity<?> getProductInRange(
            @PathVariable double min,
            @PathVariable double max){

        if(min > max){
            return ResponseEntity.status(400)
                    .body(new ApiResponse("min price must be smaller than max price"));
        }

        if(productService.getProductInRange(min,max).isEmpty()){
            return ResponseEntity.status(404)
                    .body(new ApiResponse("not found product"));
        }

        return ResponseEntity.status(200)
                .body(productService.getProductInRange(min,max));
    }

    @GetMapping("/most-purchased-product")
    public ResponseEntity<?> getMostPurchasedProducts(){
        return ResponseEntity.status(200).body(productService.getMostPurchasedProducts());
    }
    @PostMapping("/{productId}/rate/{rating}")
    public ResponseEntity<ApiResponse> rateProduct(@PathVariable String productId,
                                                   @PathVariable int rating){

        if(rating < 1 || rating > 5){
            return ResponseEntity.status(400)
                    .body(new ApiResponse("Rating must be 1-5"));
        }

        if(productService.rateProduct(productId, rating)){
            return ResponseEntity.ok(
                    new ApiResponse("Product rated successfully")
            );
        }

        return ResponseEntity.status(404)
                .body(new ApiResponse("Product not found"));
    }

    @GetMapping("/pagination")
    public ResponseEntity<?> getProductsPagination(
            @RequestParam int page,
            @RequestParam int size){

        ArrayList<Product> result =
                productService.getProductsPagination(page, size);

        if(result.isEmpty()){
            return ResponseEntity.status(404)
                    .body(new ApiResponse("No products found"));
        }

        return ResponseEntity.ok(result);
    }
}
