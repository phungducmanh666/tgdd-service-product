package services.product.api.attributeGroup.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import services.product.api.attributeGroup.repository.AttributeGroupRepository;
import services.product.data.dto.attributeGroup.AttributeGroupDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;

@Service
public class AttributeGroupService {

    private final AttributeGroupRepository attributeGroupRepository;

    public AttributeGroupService(AttributeGroupRepository attributeGroupRepository) {
        this.attributeGroupRepository = attributeGroupRepository;
    }

    public AttributeGroupDto insert(UUID categoryUid, String name) {
        return attributeGroupRepository.insert(categoryUid, name);
    }

    public FindAllResult<AttributeGroupDto> findAll(
            UUID categoryUid,
            int currentPage,
            int itemsPerPage,
            String orderField,
            OrderDirection orderDirection) {
        return attributeGroupRepository.findAll(categoryUid, currentPage, itemsPerPage, orderField, orderDirection);
    }

    public AttributeGroupDto findByUid(UUID uid) {
        return attributeGroupRepository.findByUId(uid);
    }

    public int updateName(UUID uid, String name) {
        return attributeGroupRepository.updateName(uid, name);
    }

    public int deleteByUid(UUID uid) {
        return attributeGroupRepository.deleteByUid(uid);
    }

    public boolean nameExists(String name) {
        return attributeGroupRepository.nameExists(name);
    }

}
