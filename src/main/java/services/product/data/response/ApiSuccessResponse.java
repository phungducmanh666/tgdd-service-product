package services.product.data.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiSuccessResponse<T> {
    int status;
    String message;
    T metadata;
    
    public static <T> ApiSuccessResponse<T> Success(T metadata){
        return ApiSuccessResponse.<T>builder().status(200).message("success").metadata(metadata).build();
    }

    public static <T> ApiSuccessResponse<T> Success(String message, T metadata){
        return ApiSuccessResponse.<T>builder().status(200).message(message).metadata(metadata).build();
    }

    public static <T> ApiSuccessResponse<T> Created(T metadata){
        return ApiSuccessResponse.<T>builder().status(200).message("created").metadata(metadata).build();
    }
}
