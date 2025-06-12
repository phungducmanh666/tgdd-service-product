package services.product.data.dto.productStatus;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStatusDto {

    UUID uid;
    @NotBlank(message = "product status name cannot blank")
    String name;
    Boolean isDefault;
    Date createAt;

}
