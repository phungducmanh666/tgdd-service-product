package services.product.api.category.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import services.product.api.category.repository.CategoryRepository;
import services.product.data.dto.category.CategoryDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.helper.uploader.UploaderHelper;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UploaderHelper uploaderHelper;
    private final String folderUpLoadName;

    public CategoryService(CategoryRepository categoryRepository, UploaderHelper uploaderHelper) {
        this.categoryRepository = categoryRepository;
        this.uploaderHelper = uploaderHelper;
        this.folderUpLoadName = "categories";
    }

    public CategoryDto insert(String name) {
        return categoryRepository.insert(name);
    }

    public FindAllResult<CategoryDto> findAll(
            int currentPage,
            int itemsPerPage,
            String orderField,
            OrderDirection orderDirection) {
        return categoryRepository.findAll(currentPage, itemsPerPage, orderField, orderDirection);
    }

    public CategoryDto findByUid(UUID uid) {
        return categoryRepository.findByUId(uid);
    }

    public int updateName(UUID uid, String name) {
        return categoryRepository.updateName(uid, name);
    }

    public int updatePhotoUrl(UUID uid, MultipartFile photo) {
        CategoryDto existingCategory = categoryRepository.findByUId(uid);
        String newPhotoUrl = null;
        try {
            newPhotoUrl = uploaderHelper.UploadPhoto(photo, folderUpLoadName);
            if (existingCategory.getPhotoUrl() != null && !existingCategory.getPhotoUrl().isEmpty()) {
                try {
                    uploaderHelper.DeletePhoto(existingCategory.getPhotoUrl());
                } catch (IOException e) {
                    System.err
                            .println("Warning: Failed to delete old photo for category " + uid + ": " + e.getMessage());
                }
            }
            return categoryRepository.updatePhotoUrl(uid, newPhotoUrl);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload new photo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update category photo URL: " + e.getMessage(), e);
        }
    }

    public int deleteByUid(UUID uid) {
        return categoryRepository.deleteByUid(uid);
    }

    public boolean nameExists(String name) {
        return categoryRepository.nameExists(name);
    }

}
