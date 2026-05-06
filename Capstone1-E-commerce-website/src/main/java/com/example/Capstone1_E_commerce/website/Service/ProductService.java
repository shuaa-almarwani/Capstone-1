package com.example.Capstone1_E_commerce.website.Service;

import com.example.Capstone1_E_commerce.website.Model.Category;
import com.example.Capstone1_E_commerce.website.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ProductService {

    ArrayList<Product> products = new ArrayList<>();
    private final CategoryService categoryService;

    public ArrayList<Product> getProducts(){
        return products;
    }
    public boolean isIdExists(String id){

        for(Product p : products){
            if(p.getID().equals(id)){
                return true;
            }
        }

        return false;
    }
    public boolean addProduct(Product product){

        if(isIdExists(product.getID())){
            return false; // ID already exists
        }

        boolean categoryExists = false;

        for(Category c : categoryService.getCategories()){
            if(c.getID().equals(product.getCategoryId())){
                categoryExists = true;
                break;
            }
        }

        if(!categoryExists){
            return false;
        }

        products.add(product);
        return true;
    }

    public boolean updateProduct(String id, Product product){

        boolean found = false;

        for(Product p : products){
            if(p.getID().equals(id)){
                found = true;
                break;
            }
        }

        if(!found){
            return false;
        }

        for(Product p : products){
            if(!p.getID().equals(id) && p.getID().equals(product.getID())){
                return false; // duplicate ID
            }
        }

        boolean categoryExists = false;

        for(Category c : categoryService.getCategories()){
            if(c.getID().equals(product.getCategoryId())){
                categoryExists = true;
                break;
            }
        }

        if(!categoryExists){
            return false;
        }

        for(int i = 0; i < products.size(); i++){
            if(products.get(i).getID().equals(id)){
                products.set(i, product);
                return true;
            }
        }

        return false;
    }

    public boolean deleteProduct (String id){
        for (int i = 0; i < products.size(); i++) {
            if(products.get(i).getID().equals(id))  {
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Product> getProductBasedPrice(String sortType){

        ArrayList<Product> sorted = new ArrayList<>(products);

        if(sortType.equals("asc")){
            sorted.sort(Comparator.comparing(Product::getPrice));
        }
        else if(sortType.equals("desc")){
            sorted.sort(Comparator.comparing(Product::getPrice).reversed());
        }

        return sorted;
    }

public ArrayList<Product> getProductInRange(double min , double max){
     ArrayList<Product> productInRange = new ArrayList<>();
    for (int i = 0; i < products.size(); i++) {
        if(products.get(i).getPrice() >= min && products.get(i).getPrice() <= max){
         productInRange.add(products.get(i)) ;
        }
    }
    return productInRange;
}

public Product getMostPurchasedProducts (){
        Product mostPurchasedProduct = products.get(products.size()-1);
    for (int i = 0; i < products.size(); i++) {
        if(mostPurchasedProduct.getCountOfPurchases() < products.get(i).getCountOfPurchases()){
            mostPurchasedProduct  = products.get(i);
        }
    }
    return mostPurchasedProduct;
}
    public boolean rateProduct(String productId, int rating){

        Product product = null;

        for(Product p : products){
            if(p.getID().equals(productId)){
                product = p;
                break;
            }
        }
        if(product == null){
            return false;
        }
        product.getRatings().add(rating);
        return true;
    }
    public ArrayList<Product> getProductsByCategory(String categoryId){

        ArrayList<Product> result = new ArrayList<>();

        for(Product p : products){
            if(p.getCategoryId().equals(categoryId)){
                result.add(p);
            }
        }

        return result;
    }
    public String getCategoryStats(String categoryId){
        int count = 0;
        double totalPrice = 0;
        Product mostSold = null;
        for(Product p : products){
            if(p.getCategoryId().equals(categoryId)){

                count++;
                totalPrice += p.getPrice();

                if(mostSold == null ||
                        p.getCountOfPurchases() > mostSold.getCountOfPurchases()){
                    mostSold = p;
                }
            }
        }
        if(count == 0){
            return null;
        }

        double avgPrice = totalPrice / count;

        return "Count: " + count +
                ", Avg Price: " + avgPrice +
                ", Top Product: " + mostSold.getName() +
                " (Sold: " + mostSold.getCountOfPurchases() + ")";
    }
    public ArrayList<Product> getProductsPagination(int page, int size){

        int start = (page - 1) * size;
        int end = Math.min(start + size, products.size());

        if(start >= products.size() || start < 0){
            return new ArrayList<>();
        }

        return new ArrayList<>(products.subList(start, end));
    }
}
