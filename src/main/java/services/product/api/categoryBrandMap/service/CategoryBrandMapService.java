package services.product.api.categoryBrandMap.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import services.product.api.categoryBrandMap.repository.CategoryBrandMapRepository;
import services.product.data.dto.brand.BrandDto;
import services.product.data.dto.categoryBrandMap.CategoryBrandMapDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;

@Service
public class CategoryBrandMapService {

    private final CategoryBrandMapRepository categoryBrandMapRepository;

    public CategoryBrandMapService(CategoryBrandMapRepository categoryBrandMapRepository) {
        this.categoryBrandMapRepository = categoryBrandMapRepository;
    }

    public CategoryBrandMapDto insert(UUID categoryUid, UUID brandUid) {
        return categoryBrandMapRepository.insert(categoryUid, brandUid);
    }

    public FindAllResult<BrandDto> findAllBrands(
            UUID categoryUid,
            boolean isBelong,
            int currentPage,
            int itemsPerPage,
            String orderField,
            OrderDirection orderDirection) {
        return categoryBrandMapRepository.findAllBrands(categoryUid, isBelong, currentPage, itemsPerPage, orderField,
                orderDirection);
    }

    public int delete(UUID categoryUid, UUID brandUid) {
        return categoryBrandMapRepository.delete(categoryUid, brandUid);
    }

}
