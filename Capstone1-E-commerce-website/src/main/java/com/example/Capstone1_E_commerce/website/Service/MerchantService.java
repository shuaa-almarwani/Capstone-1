package com.example.Capstone1_E_commerce.website.Service;

import com.example.Capstone1_E_commerce.website.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantService {

    ArrayList<Merchant> merchants = new ArrayList<>();


    public ArrayList<Merchant> getMerchants(){
        return merchants;
    }

    public boolean isMerchantIdExists(String id){
        for(Merchant m : merchants){
            if(m.getID().equals(id)){
                return true;
            }
        }
        return false;
    }

    public boolean addMerchant(Merchant merchant){

        if(isMerchantIdExists(merchant.getID())){
            return false;
        }

        merchants.add(merchant);
        return true;
    }
    public boolean updateMerchant(String id, Merchant merchant){

        for(Merchant m : merchants){
            if(!m.getID().equals(id) && m.getID().equals(merchant.getID())){
                return false;
            }
        }

        for(int i = 0; i < merchants.size(); i++){
            if(merchants.get(i).getID().equals(id)){
                merchants.set(i, merchant);
                return true;
            }
        }

        return false;
    }

    public boolean deleteMerchant(String id){
        for (int i = 0; i < merchants.size(); i++) {
            if(merchants.get(i).getID().equals(id)) {
                merchants.remove(i);
                return true;
            }
        }
        return false;
    }


}
