package com.example.Capstone1_E_commerce.website.Service;

import com.example.Capstone1_E_commerce.website.Model.Merchant;
import com.example.Capstone1_E_commerce.website.Model.MerchantStock;
import com.example.Capstone1_E_commerce.website.Model.Product;
import com.example.Capstone1_E_commerce.website.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class MerchantStockService {
    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();

    private final MerchantService merchantService;
    private final ProductService productService;
    private final UserService userService;


    public ArrayList<MerchantStock> getMerchantStocks() {
        return merchantStocks;
    }

    public boolean addMerchantStock(MerchantStock merchantStock){

        for(MerchantStock ms : merchantStocks){
            if(ms.getID().equals(merchantStock.getID())){
                return false;
            }
        }

        if(!validateIds(merchantStock.getMerchantId(), merchantStock.getProductId())){
            return false;
        }

        merchantStocks.add(merchantStock);
        return true;
    }

    public boolean updateMerchantStock(String id, MerchantStock merchantStock){

        if(!validateIds(merchantStock.getMerchantId(), merchantStock.getProductId())){
            return false;
        }

        for(MerchantStock ms : merchantStocks){
            if(!ms.getID().equals(id) && ms.getID().equals(merchantStock.getID())){
                return false;
            }
        }

        for(int i = 0; i < merchantStocks.size(); i++){
            if(merchantStocks.get(i).getID().equals(id)){
                merchantStocks.set(i, merchantStock);
                return true;
            }
        }

        return false;
    }

    public boolean deleteMerchantStock(String id) {
        for (int i = 0; i < merchantStocks.size(); i++) {
            if (merchantStocks.get(i).getID().equals(id)) {
                merchantStocks.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean validateIds(String merchantId, String productId) {

        boolean isMerchantExists = false;
        boolean isProductExists = false;

        for (Merchant m : merchantService.getMerchants()) {
            if (m.getID().equals(merchantId)) {
                isMerchantExists = true;
                break;
            }
        }

        for (Product p : productService.getProducts()) {
            if (p.getID().equals(productId)) {
                isProductExists = true;
                break;
            }
        }
        return isMerchantExists && isProductExists;
    }

    public boolean addStock(String merchantId, String productId, int amount) {

        for (MerchantStock ms : merchantStocks) {

            if (ms.getMerchantId().equals(merchantId)
                    && ms.getProductId().equals(productId)) {
                ms.setStock(ms.getStock() + amount);
                return true;
            }
        }
        return false;
    }

    public int buyProduct(String productId, String merchantId, String userId) {

        if (!validateIds(merchantId, productId)) {
            return 1;
        }

        User user = null;
        for (User u : userService.getUsers()) {
            if (u.getID().equals(userId)) {
                user = u;
                break;
            }
        }

        if (user == null) return 4;

        Product product = null;
        for (Product p : productService.getProducts()) {
            if (p.getID().equals(productId)) {
                product = p;
                break;
            }
        }

        if (product == null) return 5;

        MerchantStock stock = null;
        for (MerchantStock ms : merchantStocks) {
            if (ms.getMerchantId().equals(merchantId)
                    && ms.getProductId().equals(productId)) {
                stock = ms;
                break;
            }
        }

        if (stock == null || stock.getStock() <= 0) {
            return 2;
        }

        double finalPrice = product.getPrice();

// first purchase discount
        if(user.isFirstPurchase()){
            finalPrice = finalPrice * 0.9;
        }
// shipping free
        if(finalPrice < 100){
            finalPrice += 15;
        }
        if (user.getBalance() < finalPrice) {
            return 3;
        }

        user.setTotalSpent(user.getTotalSpent() + finalPrice);

        stock.setStock(stock.getStock() - 1);
        user.setBalance(user.getBalance() - finalPrice);
        product.setCountOfPurchases(product.getCountOfPurchases() + 1);

        if (user.getPurchasedProductsIDs() == null) {
            user.setPurchasedProductsIDs(new ArrayList<>());
        }
        user.getPurchasedProductsIDs().add(productId);

        if (user.isFirstPurchase()) {
            user.setFirstPurchase(false);
        }

        return 0;
    }


    //   3- Bonus ENDPOINT
    public int returnProduct(String productId, String merchantId, String userId) {

        User user = null;
        for(User u : userService.getUsers()){
            if(u.getID().equals(userId)){
                user = u;
                break;
            }
        }

        Product product = null;
        for(Product p : productService.getProducts()){
            if(p.getID().equals(productId)){
                product = p;
                break;
            }
        }

        MerchantStock stock = null;
        for(MerchantStock ms : merchantStocks){
            if(ms.getMerchantId().equals(merchantId)
                    && ms.getProductId().equals(productId)){
                stock = ms;
                break;
            }
        }

        if(user == null || product == null || stock == null){
            return 1;
        }
        if (!user.getPurchasedProductsIDs().contains(productId)) {
            return 2;
        }

        stock.setStock(stock.getStock() + 1);
        user.setBalance(user.getBalance() + product.getPrice());
        if(user.getTotalSpent() >= product.getPrice()){
            user.setTotalSpent(user.getTotalSpent() - product.getPrice());
        }

        if (user.getPurchasedProductsIDs() != null) {
            user.getPurchasedProductsIDs().remove(productId);
        }        return 0;
    }

    //   8- EXTRA ENDPOINT
    public String getProductStockStatus(String productId) {

        for(MerchantStock ms : merchantStocks){

            if(ms.getProductId().equals(productId)){

                if(ms.getStock() == 0){
                    return "OUT OF STOCK";
                }
                else if(ms.getStock() <= 5){
                    return "LOW STOCK";
                }
                else{
                    return "IN STOCK";
                }
            }
        }

        return "PRODUCT NOT FOUND";
    }

    //   9- EXTRA ENDPOINT
    public String applyDiscountAndShipping(String userId, String productId) {

        User user = null;
        for(User u : userService.getUsers()){
            if(u.getID().equals(userId)){
                user = u;
                break;
            }
        }
        Product product = null;
        for(Product p : productService.getProducts()){
            if(p.getID().equals(productId)){
                product = p;
                break;
            }
        }

        if(user == null || product == null){
            return "User or Product not found";
        }

        double price = product.getPrice();
        String message = "";

        // discount
        if(user.isFirstPurchase()){
            price = price * 0.9;
            message += "10% discount applied. ";
        }
        // shipping
        if(price > 100){
            message += "Free shipping applied. ";
        } else {
            message += "Shipping fee applied. ";
        }

        return "Final Price: " + price + " | " + message;
    }
    //   10- EXTRA ENDPOINT
    public boolean transferStock(String productId,
                                 String fromMerchantId,
                                 String toMerchantId,
                                 int amount){

        MerchantStock from = null;
        MerchantStock to = null;

        for(MerchantStock ms : merchantStocks){
            if(ms.getMerchantId().equals(fromMerchantId)
                    && ms.getProductId().equals(productId)){
                from = ms;
            }

            if(ms.getMerchantId().equals(toMerchantId)
                    && ms.getProductId().equals(productId)){
                to = ms;
            }
        }

        if(from == null || to == null){
            return false;
        }

        if(from.getStock() < amount){
            return false;
        }

        from.setStock(from.getStock() - amount);
        to.setStock(to.getStock() + amount);

        return true;
    }
}


