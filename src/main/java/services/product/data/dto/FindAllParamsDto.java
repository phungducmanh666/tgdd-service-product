package services.product.data.dto;

import lombok.Data;
import services.product.data.model.OrderDirection;

@Data
public class FindAllParamsDto {
    
    int currentPage;
    int itemsPerPage;
    String orderField;
    OrderDirection orderDirection;

}
