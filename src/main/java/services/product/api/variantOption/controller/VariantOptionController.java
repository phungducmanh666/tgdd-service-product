package services.product.api.variantOption.controller;

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
import services.product.api.variantOption.service.VariantOptionService;
import services.product.data.dto.VariantOptionDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.data.response.ApiSuccessResponse;

@RestController
@RequestMapping("/variant-options")
public class VariantOptionController {

        private final VariantOptionService variantOptionService;

        public VariantOptionController(VariantOptionService variantOptionService) {
                this.variantOptionService = variantOptionService;
        }

        @PostMapping("")
        public ResponseEntity<ApiSuccessResponse<?>> insert(
                        @RequestBody @Valid VariantOptionDto dto) {
                return ResponseEntity.status(201).body(
                                ApiSuccessResponse.Created(
                                                variantOptionService.insert(dto.getVariantAttributeUid(),
                                                                dto.getName())));
        }

        @GetMapping("/{uid}")
        public ResponseEntity<ApiSuccessResponse<?>> findByUid(
                        @PathVariable(name = "uid") UUID uid) {
                return ResponseEntity.status(200).body(
                                ApiSuccessResponse.Success(
                                                variantOptionService.findByUid(uid)));
        }

        @GetMapping("")
        public ResponseEntity<ApiSuccessResponse<?>> findAll(
                        @RequestParam(name = "variantAttributeUid", required = false) UUID variantAttributeUid,
                        @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
                        @RequestParam(name = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                        @RequestParam(name = "orderField", defaultValue = "uid") String orderField,
                        @RequestParam(name = "orderDirection", defaultValue = "ASC") OrderDirection orderDirection) {
                return ResponseEntity.ok()
                                .body(ApiSuccessResponse.<FindAllResult<VariantOptionDto>>Success("",
                                                variantOptionService.findAll(variantAttributeUid,
                                                                currentPage,
                                                                itemsPerPage,
                                                                orderField,
                                                                orderDirection)));
        }

        @PatchMapping("/{uid}/name")
        public ResponseEntity<ApiSuccessResponse<?>> update(
                        @PathVariable(name = "uid") UUID uid,
                        @RequestBody VariantOptionDto dto) {

                return ResponseEntity.status(200)
                                .body(ApiSuccessResponse.Success(
                                                variantOptionService.updateName(uid, dto.getName())));
        }

        @DeleteMapping("/{uid}")
        public ResponseEntity<ApiSuccessResponse<?>> deleteByUid(
                        @PathVariable(name = "uid") UUID uid) {
                return ResponseEntity.ok().body(
                                ApiSuccessResponse.Success(variantOptionService.deleteByUid(uid)));
        }

        @GetMapping("/exists")
        public ResponseEntity<ApiSuccessResponse<?>> nameExists(
                        @RequestParam(name = "name") String name) {
                return ResponseEntity.ok().body(
                                ApiSuccessResponse.Success(variantOptionService.nameExists(name)));
        }
}
