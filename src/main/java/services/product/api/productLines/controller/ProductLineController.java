package services.product.api.productLines.controller;

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

import jakarta.validation.Valid;
import services.product.api.productLines.service.ProductLineService;
import services.product.data.dto.ProductLineDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.data.response.ApiSuccessResponse;

@RestController
@RequestMapping("/product-lines")
public class ProductLineController {

        private final ProductLineService productLineService;

        public ProductLineController(ProductLineService productLineService) {
                this.productLineService = productLineService;
        }

        @PostMapping("")
        public ResponseEntity<ApiSuccessResponse<?>> insert(
                        @RequestBody @Valid ProductLineDto productLineDto) {
                return ResponseEntity.status(201).body(
                                ApiSuccessResponse.Created(
                                                productLineService.insert(productLineDto.getCategoryUid(),
                                                                productLineDto.getBrandUid(),
                                                                productLineDto.getName())));
        }

        @GetMapping("/{uid}")
        public ResponseEntity<ApiSuccessResponse<?>> findByUid(
                        @PathVariable(name = "uid") UUID uid) {
                return ResponseEntity.status(200).body(
                                ApiSuccessResponse.Success(
                                                productLineService.findByUid(uid)));
        }

        @GetMapping("")
        public ResponseEntity<ApiSuccessResponse<?>> findAll(
                        @RequestParam(name = "categoryUid", required = false) UUID categoryUid,
                        @RequestParam(name = "brandUid", required = false) UUID brandUid,
                        @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
                        @RequestParam(name = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                        @RequestParam(name = "orderField", defaultValue = "uid") String orderField,
                        @RequestParam(name = "orderDirection", defaultValue = "ASC") OrderDirection orderDirection) {
                return ResponseEntity.ok()
                                .body(ApiSuccessResponse.<FindAllResult<ProductLineDto>>Success("",
                                                productLineService.findAll(
                                                                categoryUid,
                                                                brandUid,
                                                                currentPage,
                                                                itemsPerPage,
                                                                orderField,
                                                                orderDirection)));
        }

        @PatchMapping("/{uid}/name")
        public ResponseEntity<ApiSuccessResponse<?>> updateName(
                        @PathVariable(name = "uid") UUID uid,
                        @RequestBody ProductLineDto productLineDto) {

                return ResponseEntity.status(200)
                                .body(ApiSuccessResponse.Success(
                                                productLineService.updateName(uid, productLineDto.getName())));
        }

        @DeleteMapping("/{uid}")
        public ResponseEntity<ApiSuccessResponse<?>> deleteByUid(
                        @PathVariable(name = "uid") UUID uid) {
                return ResponseEntity.ok().body(
                                ApiSuccessResponse.Success(productLineService.deleteByUid(uid)));
        }

        @GetMapping("/exists")
        public ResponseEntity<ApiSuccessResponse<?>> nameExists(
                        @RequestParam(name = "name") String name) {
                return ResponseEntity.ok().body(
                                ApiSuccessResponse.Success(productLineService.nameExists(name)));
        }
}
