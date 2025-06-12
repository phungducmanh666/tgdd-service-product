package services.product.data.dto.attributeGroup;

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
public class AttributeGroupDto {

    UUID uid;
    @NotBlank(message = "attribute group name cannot blank")
    String name;
    @NotNull(message = "attribute group need a category uid")
    UUID categoryUid;
    Date createAt;

}
