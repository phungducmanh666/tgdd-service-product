package services.product.api.variantAttribute.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import services.product.api.variantAttribute.repository.VariantAttributeRepository;
import services.product.data.dto.variantAttribute.VariantAttributeDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;

@Service
public class VariantAttributeService {

    private final VariantAttributeRepository variantAttributeRepository;

    public VariantAttributeService(VariantAttributeRepository variantAttributeRepository) {
        this.variantAttributeRepository = variantAttributeRepository;
    }

    public VariantAttributeDto insert(String name) {
        return variantAttributeRepository.insert(name);
    }

    public FindAllResult<VariantAttributeDto> findAll(
            int currentPage,
            int itemsPerPage,
            String orderField,
            OrderDirection orderDirection) {
        return variantAttributeRepository.findAll(currentPage, itemsPerPage, orderField, orderDirection);
    }

    public VariantAttributeDto findByUid(UUID uid) {
        return variantAttributeRepository.findByUId(uid);
    }

    public int updateName(UUID uid, String name) {
        return variantAttributeRepository.updateName(uid, name);
    }

    public int deleteByUid(UUID uid) {
        return variantAttributeRepository.deleteByUid(uid);
    }

    public boolean nameExists(String name) {
        return variantAttributeRepository.nameExists(name);
    }

}
