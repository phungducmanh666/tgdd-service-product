package services.product.api.brand.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import services.product.api.brand.service.BrandService;
import services.product.data.dto.brand.BrandDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.data.response.ApiSuccessResponse;

@RestController
@RequestMapping("/brands")
public class BrandController {

        private final BrandService brandService;

        public BrandController(BrandService brandService) {
                this.brandService = brandService;
        }

        @PostMapping("")
        public ResponseEntity<ApiSuccessResponse<?>> insert(
                        @RequestBody @Valid BrandDto brandDto) {
                return ResponseEntity.status(201).body(
                                ApiSuccessResponse.Created(
                                                brandService.insert(brandDto.getName())));
        }

        @GetMapping("/{uid}")
        public ResponseEntity<ApiSuccessResponse<?>> findByUid(
                        @PathVariable(name = "uid") UUID uid) {
                return ResponseEntity.status(200).body(
                                ApiSuccessResponse.Success(
                                                brandService.findByUid(uid)));
        }

        @GetMapping("")
        public ResponseEntity<ApiSuccessResponse<?>> findAll(
                        @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
                        @RequestParam(name = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                        @RequestParam(name = "orderField", defaultValue = "uid") String orderField,
                        @RequestParam(name = "orderDirection", defaultValue = "ASC") OrderDirection orderDirection) {
                return ResponseEntity.ok()
                                .body(ApiSuccessResponse.<FindAllResult<BrandDto>>Success("",
                                                brandService.findAll(
                                                                currentPage,
                                                                itemsPerPage,
                                                                orderField,
                                                                orderDirection)));
        }

        @PatchMapping("/{uid}/name")
        public ResponseEntity<ApiSuccessResponse<?>> update(
                        @PathVariable(name = "uid") UUID uid,
                        @RequestBody BrandDto brandDto) {

                return ResponseEntity.status(200)
                                .body(ApiSuccessResponse.Success(
                                                brandService.updateName(uid, brandDto.getName())));
        }

        @PatchMapping("/{uid}/photo")
        public ResponseEntity<ApiSuccessResponse<?>> update(
                        @PathVariable(name = "uid") UUID uid,
                        @RequestParam(name = "photo") MultipartFile photo) {

                return ResponseEntity.status(200)
                                .body(ApiSuccessResponse.Success(brandService.updatePhotoUrl(uid, photo)));
        }

        @DeleteMapping("/{uid}")
        public ResponseEntity<ApiSuccessResponse<?>> deleteByUid(
                        @PathVariable(name = "uid") UUID uid) {
                return ResponseEntity.ok().body(
                                ApiSuccessResponse.Success(brandService.deleteByUid(uid)));
        }

        @GetMapping("/exists")
        public ResponseEntity<ApiSuccessResponse<?>> nameExists(
                        @RequestParam(name = "name") String name) {
                return ResponseEntity.ok().body(
                                ApiSuccessResponse.Success(brandService.nameExists(name)));
        }
}
