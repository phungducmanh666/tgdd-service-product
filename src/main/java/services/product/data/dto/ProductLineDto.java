package services.product.data.dto;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductLineDto {

    UUID uid;
    @NotBlank(message = "name cannot blank")
    String name;
    @NotNull(message = "category uid cannot null")
    UUID categoryUid;
    @NotNull(message = "brand uid cannot null")
    UUID brandUid;
    Date createAt;
}
