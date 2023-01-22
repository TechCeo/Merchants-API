package com.MerchantAPI.logic;

import com.MerchantAPI.data.Merchant;
import com.MerchantAPI.data.MerchantRepository;
import com.MerchantAPI.data.MerchantTransaction;
import com.MerchantAPI.data.TransactionRepository;
import com.MerchantAPI.payload.request.AddTransactionRequest;
import com.MerchantAPI.payload.request.FetchTransactionRequest;
import com.MerchantAPI.payload.response.RequestResponse;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MerchantService merchantService;

    public RequestResponse handleAddTransactionRequest(AddTransactionRequest request) {
        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setResponse("Transaction saved successfully");
        requestResponse.setRequestStatus("00");

        System.out.println(request.getMerchantId());

        MerchantTransaction transaction = new MerchantTransaction();
        transaction.setTransactionAmount(request.getTransactionAmount());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setMerchantId(request.getMerchantId());
        transaction.setConnectionMode(request.getConnectionMode());
        transaction.setTransactionStatus("New");
        transactionRepository.save(transaction);


        return requestResponse;
    }

    public RequestResponse handlefetchMerchantTransaction(FetchTransactionRequest request) {
        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setRequestStatus("00");
        Long merchantId = request.getMerchantId();
        String connectionMode = request.getConnectionMode();

        if (merchantId > 0) {
            if (isConnectionMode(connectionMode)) {
                if (merchantService.isMerchant(merchantId)) {
                    List<MerchantTransaction> transactions = new ArrayList<>();
                    transactions = transactionRepository.findByMerchantId(merchantId, connectionMode);
                    requestResponse.setResponse(transactions);
                } else {
                    requestResponse.setResponse("Merchant not Found");
                }
            } else {
                requestResponse.setResponse("Please specify Live/Test connection mode");
            }
        } else {
            requestResponse.setResponse("Invalid MerchantId");
        }

        return requestResponse;
    }

    public RequestResponse handlefetchAllTransaction(FetchTransactionRequest request) {

        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setRequestStatus("00");
        Long merchantId = request.getMerchantId();
        String connectionMode = request.getConnectionMode();
        List<MerchantTransaction> transactions = new ArrayList<>();


        if (isConnectionMode(connectionMode)) {

            transactions = transactionRepository.findByConnectionMode(connectionMode);

        } else {

            transactions = transactionRepository.findAll();
        }
        requestResponse.setResponse(transactions);


        return requestResponse;
    }

    private boolean isConnectionMode(String connectionMode) {

        if(connectionMode==null){
            return  false;
        }
        return connectionMode.equalsIgnoreCase("Live") || connectionMode.equalsIgnoreCase("Test");
    }
}
