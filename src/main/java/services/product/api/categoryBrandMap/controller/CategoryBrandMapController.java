package services.product.api.categoryBrandMap.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import services.product.api.categoryBrandMap.service.CategoryBrandMapService;
import services.product.data.dto.brand.BrandDto;
import services.product.data.dto.categoryBrandMap.CategoryBrandMapDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.data.response.ApiSuccessResponse;

@RestController
@RequestMapping("/categories/{categoryUid}/brands")
public class CategoryBrandMapController {

        private final CategoryBrandMapService categoryBrandMapService;

        public CategoryBrandMapController(CategoryBrandMapService categoryBrandMapService) {
                this.categoryBrandMapService = categoryBrandMapService;
        }

        @PostMapping("/{brandUid}")
        public ResponseEntity<ApiSuccessResponse<?>> insert(@PathVariable(name = "categoryUid") UUID categoryUid,
                        @PathVariable(name = "brandUid") UUID brandUid) {
                return ResponseEntity.ok()
                                .body(ApiSuccessResponse.<CategoryBrandMapDto>Success("",
                                                categoryBrandMapService.insert(
                                                                categoryUid, brandUid)));
        }

        @GetMapping("")
        public ResponseEntity<ApiSuccessResponse<?>> findAllBrands(@PathVariable(name = "categoryUid") UUID categoryUid,
                        @RequestParam(name = "isBelong") Boolean isBelong,
                        @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
                        @RequestParam(name = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                        @RequestParam(name = "orderField", defaultValue = "uid") String orderField,
                        @RequestParam(name = "orderDirection", defaultValue = "ASC") OrderDirection orderDirection) {
                if (isBelong == null)
                        isBelong = true;
                return ResponseEntity.ok()
                                .body(ApiSuccessResponse.<FindAllResult<BrandDto>>Success("",
                                                categoryBrandMapService.findAllBrands(
                                                                categoryUid, isBelong,
                                                                currentPage,
                                                                itemsPerPage,
                                                                orderField,
                                                                orderDirection)));
        }

        @DeleteMapping("/{brandUid}")
        public ResponseEntity<ApiSuccessResponse<?>> delete(@PathVariable(name = "categoryUid") UUID categoryUid,
                        @PathVariable(name = "brandUid") UUID brandUid) {
                return ResponseEntity.ok()
                                .body(ApiSuccessResponse.<Integer>Success("",
                                                categoryBrandMapService.delete(
                                                                categoryUid, brandUid)));
        }

}
