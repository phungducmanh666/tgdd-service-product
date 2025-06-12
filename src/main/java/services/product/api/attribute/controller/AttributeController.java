package services.product.api.attribute.controller;

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
import services.product.api.attribute.service.AttributeService;
import services.product.data.dto.AttributeDto;
import services.product.data.dto.BrandDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.data.response.ApiSuccessResponse;

@RestController
@RequestMapping("/attributes")
public class AttributeController {

        private final AttributeService attributeService;

        public AttributeController(AttributeService attributeService) {
                this.attributeService = attributeService;
        }

        @PostMapping("")
        public ResponseEntity<ApiSuccessResponse<?>> insert(
                        @RequestBody @Valid AttributeDto attributeDto) {
                return ResponseEntity.status(201).body(
                                ApiSuccessResponse.Created(
                                                attributeService.insert(attributeDto.getAttributeGroupUid(),
                                                                attributeDto.getName())));
        }

        @GetMapping("/{uid}")
        public ResponseEntity<ApiSuccessResponse<?>> findByUid(
                        @PathVariable(name = "uid") UUID uid) {
                return ResponseEntity.status(200).body(
                                ApiSuccessResponse.Success(
                                                attributeService.findByUid(uid)));
        }

        @GetMapping("")
        public ResponseEntity<ApiSuccessResponse<?>> findAll(
                        @RequestParam(name = "attributeGroupUid", required = false) UUID attributeGroupUid,
                        @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
                        @RequestParam(name = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                        @RequestParam(name = "orderField", defaultValue = "uid") String orderField,
                        @RequestParam(name = "orderDirection", defaultValue = "ASC") OrderDirection orderDirection) {
                return ResponseEntity.ok()
                                .body(ApiSuccessResponse.<FindAllResult<AttributeDto>>Success("",
                                                attributeService.findAll(attributeGroupUid,
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
                                                attributeService.updateName(uid, brandDto.getName())));
        }

        @DeleteMapping("/{uid}")
        public ResponseEntity<ApiSuccessResponse<?>> deleteByUid(
                        @PathVariable(name = "uid") UUID uid) {
                return ResponseEntity.ok().body(
                                ApiSuccessResponse.Success(attributeService.deleteByUid(uid)));
        }

        @GetMapping("/exists")
        public ResponseEntity<ApiSuccessResponse<?>> nameExists(
                        @RequestParam(name = "name") String name) {
                return ResponseEntity.ok().body(
                                ApiSuccessResponse.Success(attributeService.nameExists(name)));
        }
}
