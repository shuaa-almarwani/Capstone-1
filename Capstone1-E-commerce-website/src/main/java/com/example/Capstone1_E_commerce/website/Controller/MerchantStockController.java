package com.example.Capstone1_E_commerce.website.Controller;

import com.example.Capstone1_E_commerce.website.Api.ApiResponse;
import com.example.Capstone1_E_commerce.website.Model.MerchantStock;

import com.example.Capstone1_E_commerce.website.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<ArrayList<MerchantStock>> getMerchantStocks(){
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStocks());
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addMerchantStock(@RequestBody @Valid MerchantStock merchantStock,
                                                        Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        boolean isAdded = merchantStockService.addMerchantStock(merchantStock);

        if(isAdded){
            return ResponseEntity.status(201)
                    .body(new ApiResponse("Merchant Stock added successfully"));
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse("Invalid Merchant ID, Product ID or duplicate stock ID"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateMerchantStock(@PathVariable String id,
                                                           @RequestBody @Valid MerchantStock merchantStock,
                                                           Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if(merchantStockService.updateMerchantStock(id, merchantStock)){
            return ResponseEntity.ok(
                    new ApiResponse("Merchant Stock updated successfully")
            );
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse("Invalid data or merchant stock not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMerchantStock (@PathVariable String id){

        if(merchantStockService.deleteMerchantStock(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock deleted successfully") );
        }
        return ResponseEntity.status(404).body(new ApiResponse("Merchant Stock with id "+id+" not found") );

    }


    @PatchMapping("/add-stock/{merchantId}/{productId}/{amount}")
    public ResponseEntity<ApiResponse> addStock(@PathVariable String merchantId,@PathVariable String productId,@PathVariable int amount){
        if(merchantStockService.addStock(merchantId,productId,amount)){
            return ResponseEntity.status(200).body(new ApiResponse(" Stock added successfully") );

        }
        return ResponseEntity.status(400).body(new ApiResponse("Invalid merchantId or productId") );

    }

    @PatchMapping("/buy/{merchantId}/{productId}/{userId}")
    public ResponseEntity<ApiResponse> buyProduct(
            @PathVariable String merchantId,
            @PathVariable String productId,
            @PathVariable String userId) {

        String message = "";

        int result = merchantStockService.buyProduct(productId, merchantId, userId);
        switch (result) {

            case 0:
                return ResponseEntity.status(200)
                        .body(new ApiResponse("Purchase successful"));

            case 1:
                message = "Invalid merchantId or productId";
                break;

            case 2:
                message = "Out of stock";
                break;

            case 3:
                message = "\"Not enough balance";
                break;

            case 4:
                message = "User not found";
                break;

            case 5:
                message = "Product not found";
                break;
        }

        return ResponseEntity.status(400).body(new ApiResponse(message));
    }

    @PatchMapping("/return/{merchantId}/{productId}/{userId}")
    public ResponseEntity<ApiResponse> returnProduct(
            @PathVariable String merchantId,
            @PathVariable String productId,
            @PathVariable String userId) {

        int result = merchantStockService.returnProduct(productId, merchantId, userId);

        String message = "";

        switch (result) {

            case 0:
                return ResponseEntity.status(200)
                        .body(new ApiResponse("Product returned successfully"));

            case 1:
                message = "Invalid userId, productId, or merchantId";
                break;
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("user cannot return a product he haven't purchased"));
            default:
                message = "Something went wrong";
                break;
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse(message));
    }

    @GetMapping("/status/{productId}")
    public ResponseEntity<?> getStockStatus(@PathVariable String productId){

        String result = merchantStockService.getProductStockStatus(productId);

        return ResponseEntity.ok(new ApiResponse(result));
    }

    @GetMapping("/discount/{userId}/{productId}")
    public ResponseEntity<?> applyDiscount(
            @PathVariable String userId,
            @PathVariable String productId){

        String result = merchantStockService.applyDiscountAndShipping(userId, productId);

        return ResponseEntity.ok(new ApiResponse(result));
    }
    @PutMapping("/transfer/{productId}/{fromId}/{toId}/{amount}")
    public ResponseEntity<ApiResponse> transferStock(@PathVariable String productId,
                                                     @PathVariable String fromId,
                                                     @PathVariable String toId,
                                                     @PathVariable int amount){
        if(merchantStockService.transferStock(productId, fromId, toId, amount)){
            return ResponseEntity.ok(
                    new ApiResponse("Stock transferred successfully")
            );
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse("Transfer failed"));
    }

}
