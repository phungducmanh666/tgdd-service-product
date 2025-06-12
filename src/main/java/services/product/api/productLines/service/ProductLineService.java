package services.product.api.productLines.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import services.product.api.productLines.repository.ProductLineRepository;
import services.product.data.dto.productLine.ProductLineDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;

@Service
public class ProductLineService {

    private final ProductLineRepository productLineRepository;

    public ProductLineService(ProductLineRepository productLineRepository) {
        this.productLineRepository = productLineRepository;
    }

    public ProductLineDto insert(UUID categoryUid, UUID brandUid, String name) {
        return productLineRepository.insert(categoryUid, brandUid, name);
    }

    public ProductLineDto findByUid(UUID uid) {
        return productLineRepository.findByUId(uid);
    }

    public FindAllResult<ProductLineDto> findAll(UUID categoryUid, UUID brandUid, int page, int size, String orderField,
            OrderDirection orderDirection) {
        return productLineRepository.findAll(categoryUid, brandUid, page, size, orderField, orderDirection);
    }

    public int updateName(UUID uid, String name) {
        return productLineRepository.updateName(uid, name);
    }

    public int deleteByUid(UUID uid) {
        return productLineRepository.deleteByUid(uid);
    }

    public boolean nameExists(String name) {
        return productLineRepository.nameExists(name);
    }

}
