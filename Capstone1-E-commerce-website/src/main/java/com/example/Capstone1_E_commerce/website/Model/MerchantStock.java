package com.example.Capstone1_E_commerce.website.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {
    @NotEmpty(message = "Merchant ID must not be empty")
    private String ID;
    @NotEmpty(message = "product ID must not be empty")
    private String productId;
    @NotEmpty(message = " merchant ID must not be empty")
    private String merchantId;
    @NotNull(message = "Stock must not be empty")
    @Min(value = 11,message = "Stock must be more than 10 at Start")
    private int stock;
}
