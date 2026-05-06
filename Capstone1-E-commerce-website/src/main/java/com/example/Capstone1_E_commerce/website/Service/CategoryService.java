package com.example.Capstone1_E_commerce.website.Service;

import com.example.Capstone1_E_commerce.website.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {


    ArrayList<Category> categories = new ArrayList<>();


    public ArrayList<Category> getCategories(){
        return categories;
    }

    public boolean isCategoryIdExists(String id){
        for(Category c : categories){
            if(c.getID().equals(id)){
                return true;
            }
        }
        return false;
    }

    public boolean addCategory(Category category){

        if(isCategoryIdExists(category.getID())){
            return false;
        }

        categories.add(category);
        return true;
    }

    public boolean updateCategory(String id, Category category){

        for(Category c : categories){
            if(!c.getID().equals(id) && c.getID().equals(category.getID())){
                return false;
            }
        }

        for(int i = 0; i < categories.size(); i++){
            if(categories.get(i).getID().equals(id)){
                categories.set(i, category);
                return true;
            }
        }

        return false;
    }

    public boolean deleteCategory(String id){
        for (int i = 0; i<categories.size();i++) {
            if(categories.get(i).getID().equals(id)){
                categories.remove(i);
                return true;
            }
        }
        return false;
    }

}
