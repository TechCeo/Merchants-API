package com.portfolio.merchantapi.merchant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<MerchantResponse> findMerchantById(Long id) {
        return merchantRepository.findById(id)
                .map(MerchantResponse::from);
    }

    public boolean existsById(Long id) {
        return merchantRepository.existsById(id);
    }
}
