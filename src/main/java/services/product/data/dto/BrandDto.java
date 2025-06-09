package services.product.data.dto;

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
public class BrandDto {

    UUID uid;
    @NotBlank(message = "brand name cannot blank")
    String name;
    String photoUrl;

}
