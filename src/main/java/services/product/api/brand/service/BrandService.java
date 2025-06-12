package services.product.api.brand.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import services.product.api.brand.repository.BrandRepository;
import services.product.data.dto.brand.BrandDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.helper.uploader.UploaderHelper;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final UploaderHelper uploaderHelper;
    private final String folderUpLoadName;

    public BrandService(BrandRepository brandRepository, UploaderHelper uploaderHelper) {
        this.brandRepository = brandRepository;
        this.uploaderHelper = uploaderHelper;
        this.folderUpLoadName = "brands";
    }

    public BrandDto insert(String name) {
        return brandRepository.insert(name);
    }

    public FindAllResult<BrandDto> findAll(
            int currentPage,
            int itemsPerPage,
            String orderField,
            OrderDirection orderDirection) {
        return brandRepository.findAll(currentPage, itemsPerPage, orderField, orderDirection);
    }

    public BrandDto findByUid(UUID uid) {
        return brandRepository.findByUId(uid);
    }

    public int updateName(UUID uid, String name) {
        return brandRepository.updateName(uid, name);
    }

    public int updatePhotoUrl(UUID uid, MultipartFile photo) {
        BrandDto existingCategory = brandRepository.findByUId(uid);
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
            return brandRepository.updatePhotoUrl(uid, newPhotoUrl);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload new photo: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update category photo URL: " + e.getMessage(), e);
        }
    }

    public int deleteByUid(UUID uid) {
        return brandRepository.deleteByUid(uid);
    }

    public boolean nameExists(String name) {
        return brandRepository.nameExists(name);
    }

}
