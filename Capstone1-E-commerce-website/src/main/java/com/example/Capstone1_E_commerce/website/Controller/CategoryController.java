package com.example.Capstone1_E_commerce.website.Controller;

import com.example.Capstone1_E_commerce.website.Api.ApiResponse;
import com.example.Capstone1_E_commerce.website.Model.Category;
import com.example.Capstone1_E_commerce.website.Service.CategoryService;
import com.example.Capstone1_E_commerce.website.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<ArrayList<Category>> getCategories(){
        return ResponseEntity.status(200).body(categoryService.getCategories());
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody @Valid Category category, Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        boolean result = categoryService.addCategory(category);

        if(result){
            return ResponseEntity.status(201)
                    .body(new ApiResponse("Category added successfully"));
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse("Category ID already exists"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable String id,
                                                      @RequestBody @Valid Category category,
                                                      Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if(categoryService.updateCategory(id, category)){
            return ResponseEntity.ok(
                    new ApiResponse("Category updated successfully")
            );
        }

        return ResponseEntity.status(404)
                .body(new ApiResponse("Category not found"));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String id){
        if(categoryService.deleteCategory(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("Category with id "+id+" not found"));

    }
    @GetMapping("/{categoryId}/stats")
    public ResponseEntity<ApiResponse> getCategoryStats(@PathVariable String categoryId){

        String result = productService.getCategoryStats(categoryId);

        if(result == null){
            return ResponseEntity.status(404)
                    .body(new ApiResponse("Category has no products"));
        }

        return ResponseEntity.ok(new ApiResponse(result));
    }
}
