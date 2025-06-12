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
public class AttributeDto {

    UUID uid;
    @NotBlank(message = "attribute name cannot blank")
    String name;
    @NotNull(message = "attribute need a attribute group uid")
    UUID attributeGroupUid;
    Date createAt;
}
