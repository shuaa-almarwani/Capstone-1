package com.example.Capstone1_E_commerce.website.Model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
    @NotEmpty(message = "ID must not be empty")
    private String ID;
    @NotEmpty(message = "user Name must not be empty")
    @Size(min = 6, message = "user Name have to be more than 5 length")
    private String username;
    @NotEmpty(message = "password  must not be empty")
    @Size(min = 7, message = "password must be more than 6 length")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "password must contain letters and digits"
    )
    private String password;
    @NotEmpty(message = "email must not be empty")
    @Email(message = "Email must be valid email")
    private String email;
    @NotEmpty(message = "role must not be empty")
    @Pattern(regexp = "(?i)^(Admin|Customer)$", message = "Role have to be 'Admin' or 'Customer' only")
    private String role;
    @PositiveOrZero(message = "balance must be a Positive number or Zero")
    private double balance;

    private double totalSpent = 0;
//    private int totalPurchases = 0;
    private boolean firstPurchase = true;

    private ArrayList<Product> wishList = new ArrayList<>();

    private ArrayList<String> purchasedProductsIDs = new ArrayList<>();

}
