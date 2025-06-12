package services.product.api.variantOption.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import services.product.api.variantOption.repository.VariantOptionRepository;
import services.product.data.dto.VariantOptionDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;

@Service
public class VariantOptionService {

    private final VariantOptionRepository variantOptionRepository;

    public VariantOptionService(VariantOptionRepository variantOptionRepository) {
        this.variantOptionRepository = variantOptionRepository;
    }

    public VariantOptionDto insert(UUID categoryUid, String name) {
        return variantOptionRepository.insert(categoryUid, name);
    }

    public FindAllResult<VariantOptionDto> findAll(
            UUID variantAttributeUid,
            int currentPage,
            int itemsPerPage,
            String orderField,
            OrderDirection orderDirection) {
        return variantOptionRepository.findAll(variantAttributeUid, currentPage, itemsPerPage, orderField,
                orderDirection);
    }

    public VariantOptionDto findByUid(UUID uid) {
        return variantOptionRepository.findByUId(uid);
    }

    public int updateName(UUID uid, String name) {
        return variantOptionRepository.updateName(uid, name);
    }

    public int deleteByUid(UUID uid) {
        return variantOptionRepository.deleteByUid(uid);
    }

    public boolean nameExists(String name) {
        return variantOptionRepository.nameExists(name);
    }

}
