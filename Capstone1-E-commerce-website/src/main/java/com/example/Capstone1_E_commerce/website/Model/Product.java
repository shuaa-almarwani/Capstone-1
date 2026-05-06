package com.example.Capstone1_E_commerce.website.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @NotEmpty(message = "ID must not be empty")
   private String ID;
    @NotEmpty(message = "name must not be empty")
    @Size(min=4, message = "name have to be more than 3 length")
   private String name;
    @NotNull(message = "price must not be empty")
    @Positive(message = "Price must be positive Number")
   private double price;
    @NotEmpty(message = "categoryId must not be empty")
   private String categoryId;


    private int countOfPurchases ;
    private ArrayList<Integer> ratings = new ArrayList<>();
}
