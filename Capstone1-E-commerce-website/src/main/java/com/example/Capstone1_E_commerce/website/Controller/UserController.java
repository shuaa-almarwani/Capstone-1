package com.example.Capstone1_E_commerce.website.Controller;

import com.example.Capstone1_E_commerce.website.Api.ApiResponse;
import com.example.Capstone1_E_commerce.website.Model.User;

import com.example.Capstone1_E_commerce.website.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/get")
    public ResponseEntity<ArrayList<User>> getUsers() {
        return ResponseEntity.status(200).body(userService.getUsers());
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody @Valid User user, Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        boolean result = userService.addUser(user);

        if(result){
            return ResponseEntity.status(201)
                    .body(new ApiResponse("User added successfully"));
        }

        return ResponseEntity.status(400)
                .body(new ApiResponse("User ID already exists"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable String id,
                                                  @RequestBody @Valid User user,
                                                  Errors errors){

        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        if(userService.updateUser(id, user)){
            return ResponseEntity.ok(
                    new ApiResponse("User updated successfully")
            );
        }

        return ResponseEntity.status(404)
                .body(new ApiResponse("User not found"));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.status(200).body(new ApiResponse("user deleted successfully "));
        }
        return ResponseEntity.status(404).body(new ApiResponse("user with id " + id + " not found"));

    }

@PatchMapping("/change-password/{id}")
public ResponseEntity<ApiResponse> changePassword(
        @PathVariable String id,
        @RequestBody String newPassword) {

    final String cleanedPassword = newPassword.replace("\"", "").trim();

    if (cleanedPassword.isEmpty()) {
        return ResponseEntity.badRequest().body(new ApiResponse("Password must not be empty"));
    }

    if (cleanedPassword.length() < 7) {
        return ResponseEntity.badRequest().body(new ApiResponse("Password must be more than 6 characters"));
    }

    if (!cleanedPassword.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
        return ResponseEntity.badRequest().body(new ApiResponse("Password must contain letters and digits"));
    }
    int result = userService.changePassword(id, cleanedPassword);
    switch (result) {

        case 1:
            return ResponseEntity.ok(
                    new ApiResponse("Password changed successfully")
            );

        case 2:
            return ResponseEntity.badRequest().body(
                    new ApiResponse("New password must be different from old password")
            );

        case 3:
            return ResponseEntity.status(404).body(
                    new ApiResponse("User with id " + id + " not found")
            );

        default:
            return ResponseEntity.internalServerError().body(
                    new ApiResponse("An unexpected error occurred")
            );
    }
}
    @GetMapping("/admin/dashboard/{userId}")
    public ResponseEntity<?> dashboard(@PathVariable String userId){

        if(!userService.isAdmin(userId)){
            return ResponseEntity.status(403)
                    .body(new ApiResponse("Access not allowed  - Admin only"));
        }

        return ResponseEntity.ok(
                new ApiResponse(userService.getDashboard())
        );
    }

    @PostMapping("/{userId}/wish-list/{productId}")
    public ResponseEntity<ApiResponse> addToWishList(@PathVariable String userId,
                                                 @PathVariable String productId){
        if(userService.addToWishList(userId, productId)){
            return ResponseEntity.ok(
                    new ApiResponse("Product added to wish list")
            );
        }
        return ResponseEntity.status(400)
                .body(new ApiResponse("User or Product not found / already in your wish list "));
    }

    @GetMapping("/wish-list/{userId}")
    public ResponseEntity<?> getWishList(@PathVariable String userId){
        if(!userService.isUserIdExists(userId)){
            return ResponseEntity.status(404)
                    .body(new ApiResponse("user with id "+userId+" not found"));
        }
        if(userService.getWishList(userId).isEmpty()){
            return ResponseEntity.status(404)
                    .body(new ApiResponse("not found any product in wish list"));
        }

        return ResponseEntity.status(200)
                .body(userService.getWishList(userId));
    }
    @GetMapping("/{adminId}/analysis/{userId}")
    public ResponseEntity<ApiResponse> userAnalysis(@PathVariable String adminId,@PathVariable String userId){
        boolean adminIdResult = userService.isUserIdExists(adminId);
        if(!adminIdResult){
            return ResponseEntity.status(404)
                    .body(new ApiResponse("Admin with id "+adminId+"  not found"));
        }
        boolean userIdExists = userService.isUserIdExists(userId);
        if(!userIdExists){
            return ResponseEntity.status(404)
                    .body(new ApiResponse("user with id "+userId+" not found"));
        }
        if(!userService.isAdmin(adminId)){
            return ResponseEntity.status(403)
                    .body(new ApiResponse("Access not allowed  - Admin only"));
        }
        return ResponseEntity.ok(new ApiResponse(userService.getUserAnalysis(adminId,userId)));
    }



        @PostMapping("/{userId}/rate/{productId}/{rating}")
        public ResponseEntity<ApiResponse> rateProduct(
                @PathVariable String userId,
                @PathVariable String productId,
                @PathVariable int rating) {
            int result = userService.rateProduct(userId, productId, rating);
                    if(rating < 1 || rating > 5){
            return ResponseEntity.status(400)
                    .body(new ApiResponse("Rating must be 1-5"));
        }
            switch (result) {
                case 0:
                    return ResponseEntity.ok(new ApiResponse("Product rated successfully"));
                case 1:
                    return ResponseEntity.status(404).body(new ApiResponse("User not found"));
                case 2:
                    return ResponseEntity.status(403).body(new ApiResponse("Must purchase product before rating"));
                case 3:
                    return ResponseEntity.status(404).body(new ApiResponse("Product not found"));
                default:
                    return ResponseEntity.status(400).body(new ApiResponse("Bad request"));
            }
        }

}
