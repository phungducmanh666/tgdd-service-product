package services.product.api.attribute.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import services.product.api.attribute.repository.AttributeRepository;
import services.product.data.dto.attribute.AttributeDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    public AttributeDto insert(UUID attributeGroupUid, String name) {
        return attributeRepository.insert(attributeGroupUid, name);
    }

    public FindAllResult<AttributeDto> findAll(
            UUID attributeGroupUid,
            int currentPage,
            int itemsPerPage,
            String orderField,
            OrderDirection orderDirection) {
        return attributeRepository.findAll(attributeGroupUid, currentPage, itemsPerPage, orderField, orderDirection);
    }

    public AttributeDto findByUid(UUID uid) {
        return attributeRepository.findByUId(uid);
    }

    public int updateName(UUID uid, String name) {
        return attributeRepository.updateName(uid, name);
    }

    public int deleteByUid(UUID uid) {
        return attributeRepository.deleteByUid(uid);
    }

    public boolean nameExists(String name) {
        return attributeRepository.nameExists(name);
    }

}
