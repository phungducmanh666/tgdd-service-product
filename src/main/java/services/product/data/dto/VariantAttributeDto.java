package services.product.data.dto;

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
public class VariantAttributeDto {

    UUID uid;
    @NotBlank(message = "variant attribute name cannot blank")
    String name;
    Date createAt;
}
