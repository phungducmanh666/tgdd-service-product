package services.product.data.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FindAllResult<T> {
    List<T> data;
    Pagination pagination;
}


