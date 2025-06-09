package services.product.data.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiErrorResponse {
    String code;
    String message;
}
