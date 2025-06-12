package services.product.api.categoryBrandMap.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import services.product.data.dto.brand.BrandDto;
import services.product.data.dto.brand.BrandRowMapper;
import services.product.data.dto.categoryBrandMap.CategoryBrandMapDto;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.data.model.Pagination;
import services.product.exception.custome.AlreadyExistsException;
import services.product.exception.custome.NotFoundException;
import services.product.helper.convertor.ConvertorHelper;
import services.product.helper.generator.GeneratorHelper;

@Repository
public class CategoryBrandMapRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String brandsTableName;
    private final String categoryBrandMapTableName;
    private final Set<String> allowedOrderFields;

    public CategoryBrandMapRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.brandsTableName = "brands";
        this.categoryBrandMapTableName = "category_brand_map";
        this.allowedOrderFields = Set.of("uid", "name");
    }

    public CategoryBrandMapDto insert(UUID categoryUid, UUID brandUid) {
        UUID uid = GeneratorHelper.RandomUUID();
        String sql = String.format("INSERT INTO %s (uid, category_uid, brand_uid) VALUES (?, ?, ?)",
                categoryBrandMapTableName);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, uid.toString());
                ps.setString(2, categoryUid.toString());
                ps.setString(3, brandUid.toString());
                return ps;
            }, keyHolder);

            CategoryBrandMapDto result = new CategoryBrandMapDto();
            result.setCategoryUid(categoryUid);
            result.setBrandUid(brandUid);

            if (keyHolder.getKey() != null) {
                result.setUid(ConvertorHelper.String2UUID(keyHolder.getKey().toString()));
            }
            return result;
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistsException(e.getMessage());
        }
    }

    public FindAllResult<BrandDto> findAllBrands(UUID categoryUid, boolean isBelong, int page, int size,
            String orderField, OrderDirection orderDirection) {
        String sanitizedOrderField = allowedOrderFields.contains(orderField) ? orderField : "uid";
        String orderByClause = String.format(" ORDER BY b.`%s` %s", sanitizedOrderField,
                (orderDirection != null ? orderDirection.getName() : "ASC"));

        int currentPage = Math.max(1, page);
        int offset = (currentPage - 1) * size;
        if (offset < 0) {
            offset = 0;
        }
        String paginationClause = String.format(" LIMIT %d OFFSET %d", size, offset);

        StringBuilder whereClause = new StringBuilder();
        // Danh sách các tham số sẽ được truyền vào truy vấn
        List<Object> params = new java.util.ArrayList<>();

        // Logic để lấy brands thuộc về hoặc không thuộc về categoryUid
        if (categoryUid != null) {
            if (isBelong) {
                // Lấy các brands thuộc về categoryUid
                whereClause.append(" WHERE b.uid IN (SELECT cbm.brand_uid FROM ").append(categoryBrandMapTableName)
                        .append(" cbm WHERE cbm.category_uid = ?)");
                params.add(categoryUid.toString());
            } else {
                // Lấy các brands KHÔNG thuộc về categoryUid
                whereClause.append(" WHERE b.uid NOT IN (SELECT cbm.brand_uid FROM ").append(categoryBrandMapTableName)
                        .append(" cbm WHERE cbm.category_uid = ?)");
                params.add(categoryUid.toString());
            }
        }
        // Nếu categoryUid là null, nó sẽ lấy tất cả brands (không có điều kiện where)

        String dataSql = String.format("SELECT b.* FROM %s b %s %s %s", brandsTableName, whereClause.toString(),
                orderByClause, paginationClause);
        List<BrandDto> data = jdbcTemplate.query(dataSql, new BrandRowMapper(), params.toArray());

        // Lấy tổng số lượng items
        String countSql = String.format("SELECT COUNT(b.uid) FROM %s b %s", brandsTableName, whereClause.toString());
        Long totalItems = jdbcTemplate.queryForObject(countSql, Long.class, params.toArray());
        if (totalItems == null) {
            totalItems = 0L;
        }

        int totalPages = (int) Math.ceil((double) totalItems / size);
        if (totalPages == 0 && totalItems > 0) {
            totalPages = 1;
        }

        Pagination paginationInfo = Pagination.builder()
                .currentPage(currentPage)
                .itemsPerPage(size)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .build();

        return FindAllResult.<BrandDto>builder()
                .data(data)
                .pagination(paginationInfo)
                .build();
    }

    public int delete(UUID categoryUid, UUID brandUid) {
        String sql = String.format("DELETE FROM %s WHERE category_uid = ? AND brand_uid = ?",
                categoryBrandMapTableName);
        int rowsAffected = jdbcTemplate.update(sql, categoryUid.toString(), brandUid.toString());
        if (rowsAffected == 0) {
            throw new NotFoundException(
                    String.format("category brand map not found %s - %s", categoryUid.toString(), brandUid.toString()));
        }
        return rowsAffected;
    }
}
