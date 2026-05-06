package com.example.Capstone1_E_commerce.website.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Merchant {
    @NotEmpty(message = "ID must not be empty")
    private String ID;
    @NotEmpty(message = "name must not be empty")
    @Size(min=4, message = "name have to be more than 3 length")
   private String name;
}
