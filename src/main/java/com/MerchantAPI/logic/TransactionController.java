package com.MerchantAPI.logic;

import com.MerchantAPI.data.TransactionRepository;
import com.MerchantAPI.payload.request.AddMerchantRequest;
import com.MerchantAPI.payload.request.AddTransactionRequest;
import com.MerchantAPI.payload.request.FetchTransactionRequest;
import com.MerchantAPI.payload.response.RequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST, value = "/addTransaction")
    public RequestResponse addTransaction(@RequestBody AddTransactionRequest request) {
        return transactionService.handleAddTransactionRequest(request);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/fetchMerchantTransaction")
    public RequestResponse fetchMerchantTransaction(@RequestBody FetchTransactionRequest request) {
        return transactionService.handlefetchMerchantTransaction(request);
    }
    @RequestMapping(method = RequestMethod.POST, value = "/fetchAllTransaction")
    public RequestResponse fetchAllTransaction(@RequestBody FetchTransactionRequest request) {
        return transactionService.handlefetchAllTransaction(request);
    }
}
