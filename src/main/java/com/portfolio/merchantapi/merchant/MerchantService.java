package com.portfolio.merchantapi.merchant;

import com.portfolio.merchantapi.common.exception.MerchantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantResponse createMerchant(MerchantRequest request) {
        Merchant merchant = new Merchant();
        merchant.setMerchantName(request.merchantName());
        merchant.setMerchantAddress(request.merchantAddress());

        return MerchantResponse.from(merchantRepository.save(merchant));
    }

    public MerchantResponse findMerchantById(Long id) {
        return merchantRepository.findById(id)
                .map(MerchantResponse::from)
                .orElseThrow(() -> new MerchantNotFoundException(id));
    }

    public boolean existsById(Long id) {
        return merchantRepository.existsById(id);
    }
}
