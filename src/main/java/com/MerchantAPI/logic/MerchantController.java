package com.MerchantAPI.logic;

import com.MerchantAPI.payload.request.AddMerchantRequest;
import com.MerchantAPI.payload.response.RequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MerchantController {

    @Autowired
    private MerchantService merchantService;


    @RequestMapping(method = RequestMethod.POST, value = "/addMerchant")
    public RequestResponse addMerchant(@RequestBody AddMerchantRequest request){
        return merchantService.handleAddMerchantRequest(request);
    }
}
