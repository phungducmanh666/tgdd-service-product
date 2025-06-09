package services.product.data.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryBrandMapDto {

    UUID uid;
    UUID categoryUid;
    UUID brandUid;

}
