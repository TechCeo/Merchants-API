package com.MerchantAPI.logic;

import com.MerchantAPI.data.Merchant;
import com.MerchantAPI.data.MerchantRepository;
import com.MerchantAPI.payload.request.AddMerchantRequest;
import com.MerchantAPI.payload.response.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {
    @Autowired
    private MerchantRepository merchantRepository;
    public RequestResponse handleAddMerchantRequest(AddMerchantRequest request) {
        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setResponse("Merchant saved successfully");
        requestResponse.setRequestStatus("00");

        Merchant merchant = new Merchant();
        merchant.setMerchantName(request.getMerchantName());
        merchant.setMerchantAddress(request.getMerchantAddress());
        merchantRepository.save(merchant);


        return requestResponse;
    }

    public boolean isMerchant(Long id) {
        return merchantRepository.existsById(id);
    }
}
