package com.example.Capstone1_E_commerce.website.Service;

import com.example.Capstone1_E_commerce.website.Model.Product;
import com.example.Capstone1_E_commerce.website.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor

public class UserService {

    ArrayList<User> users = new ArrayList<>();
    private final MerchantService merchantService;
    private final ProductService productService;


    public ArrayList<User> getUsers()
    {
        return users;

    }
    public boolean isUserIdExists(String id){
        for(User u : users){
            if(u.getID().equals(id)){
                return true;
            }
        }
        return false;
    }

    public boolean addUser(User user){

        if(isUserIdExists(user.getID())){
            return false;
        }
        users.add(user);
        return true;
    }

    public boolean updateUser(String id, User user){

        for(User u : users){
            if(!u.getID().equals(id) && u.getID().equals(user.getID())){
                return false;
            }
        }

        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getID().equals(id)){
                users.set(i, user);
                return true;
            }
        }

        return false;
    }
    public boolean deleteUser (String id){
        for(int i = 0; i < users.size();i++){
            if(users.get(i).getID().equals(id)){
                users.remove(i);
                return true;
            }
        }
        return false;
    }
    public int changePassword(String id,String newPassword){
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getID().equals(id)){
                if(!newPassword.equals(users.get(i).getPassword())) {
                    users.get(i).setPassword(newPassword);
                    return 1;
                }
                else {
                    return 2;
                }
            }

        }
        return 3;
    }

    public boolean isAdmin(String userId){

        for(User u : users){
            if(u.getID().equals(userId)){
                return u.getRole().equals("Admin");
            }
        }

        return false;
    }
    public String getDashboard(){

        int totalUsers = users.size();
        int totalProducts = productService.getProducts().size();
        int totalMerchants = merchantService.getMerchants().size();
        int totalSales = 0;
        double revenue = 0;

        for(Product p : productService.getProducts()){
            totalSales += p.getCountOfPurchases();
            revenue += p.getCountOfPurchases() * p.getPrice();
        }
        return "Users: " + totalUsers +
                ", Products: " + totalProducts +
                ", Merchants: " + totalMerchants +
                ", Sales: " + totalSales +
                ", Revenue: " + revenue;
    }

    public boolean addToWishList(String userId, String productId){

        User user = null;
        Product product = null;
        for(User u : users){
            if(u.getID().equals(userId)){
                user = u;
                break;
            }
        }
        for(Product p : productService.getProducts()){
            if(p.getID().equals(productId)){
                product = p;
                break;
            }
        }
        if(user == null || product == null){
            return false;
        }
        if(user.getWishList() == null){
            user.setWishList(new ArrayList<>());
        }
        for(Product p : user.getWishList()){
            if(p.getID().equals(productId)){
                return false;
            }
        }
        user.getWishList().add(product);

        return true;
    }

    public ArrayList<Product> getWishList(String userId){

        for(User u : users){

            if(u.getID().equals(userId)){

                if(u.getWishList() == null){
                    u.setWishList(new ArrayList<>());
                }

                return u.getWishList();
            }
        }

        return new ArrayList<>();
    }

    public String getUserAnalysis(String adminId,String userId){

        User user = null;
        for(User u : users){
            if(u.getID().equals(userId)){
                user = u;
                break;
            }
        }
        if(user == null){
            return null;
        }

        double spent = user.getTotalSpent();

        String level;

        if(spent < 100){
            level = "Low Spender";
        } else if(spent < 500){
            level = "Medium Spender";
        } else {
            level = "High Spender";
        }

        return "Spent: " + spent + ", Level: " + level;
    }
}
