package com.example.Capstone1_E_commerce.website.Controller;

import com.example.Capstone1_E_commerce.website.Api.ApiResponse;
import com.example.Capstone1_E_commerce.website.Model.Merchant;
import com.example.Capstone1_E_commerce.website.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/get")
    public ResponseEntity<ArrayList<Merchant>> getMerchants(){
        return ResponseEntity.status(200).body( merchantService.getMerchants());
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addMerchant(@RequestBody @Valid Merchant merchant, Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        boolean result = merchantService.addMerchant(merchant);

        if(result){
            return ResponseEntity.status(201)
                    .body(new ApiResponse("Merchant added successfully"));
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse("Merchant ID already exists"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateMerchant(@PathVariable String id,
                                                      @RequestBody @Valid Merchant merchant,
                                                      Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if(merchantService.updateMerchant(id, merchant)){
            return ResponseEntity.ok(
                    new ApiResponse("Merchant updated successfully")
            );
        }

        return ResponseEntity.status(404)
                .body(new ApiResponse("Merchant not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMerchant (@PathVariable String id){

        if(merchantService.deleteMerchant(id)){
            return ResponseEntity.status(200).body(new ApiResponse("merchant deleted successfully") );
        }
        return ResponseEntity.status(404).body(new ApiResponse("merchant with id "+id+" not found") );

    }

}
