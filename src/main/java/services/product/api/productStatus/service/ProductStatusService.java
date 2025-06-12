package services.product.api.productStatus.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import services.product.api.productStatus.repository.ProductStatusRepository;
import services.product.data.dto.productStatus.ProductStatusDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;

@Service
public class ProductStatusService {

    private final ProductStatusRepository productStatusRepository;

    public ProductStatusService(ProductStatusRepository productStatusRepository) {
        this.productStatusRepository = productStatusRepository;
    }

    public ProductStatusDto insert(String name) {
        return productStatusRepository.insert(name);
    }

    public FindAllResult<ProductStatusDto> findAll(
            int currentPage,
            int itemsPerPage,
            String orderField,
            OrderDirection orderDirection) {
        return productStatusRepository.findAll(currentPage, itemsPerPage, orderField, orderDirection);
    }

    public ProductStatusDto findByUid(UUID uid) {
        return productStatusRepository.findByUId(uid);
    }

    public int updateName(UUID uid, String name) {
        return productStatusRepository.updateName(uid, name);
    }

    public int setDefault(UUID uid) {
        return productStatusRepository.setDefault(uid);
    }

    public int deleteByUid(UUID uid) {
        return productStatusRepository.deleteByUid(uid);
    }

    public boolean nameExists(String name) {
        return productStatusRepository.nameExists(name);
    }

}
