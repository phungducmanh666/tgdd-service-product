package services.product.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pagination {
    int currentPage;
    int itemsPerPage;
    int totalPages;
    long totalItems;
}
