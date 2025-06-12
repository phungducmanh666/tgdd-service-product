package services.product.api.productStatus.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import services.product.data.dto.productStatus.ProductStatusDto;
import services.product.data.dto.productStatus.ProductStatusRowMapper;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.data.model.Pagination;
import services.product.exception.custome.AlreadyExistsException;
import services.product.exception.custome.NotFoundException;
import services.product.helper.convertor.ConvertorHelper;
import services.product.helper.generator.GeneratorHelper;

@Repository
public class ProductStatusRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final Map<String, String> allowedOrderFields;

    public ProductStatusRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = "product_status";
        this.allowedOrderFields = Map.of(
                "uid", "uid",
                "name", "name",
                "isDefault", "is_default",
                "createAt", "create_at");
    }

    public ProductStatusDto insert(String name) {
        ProductStatusDto item = ProductStatusDto.builder()
                .uid(GeneratorHelper.RandomUUID())
                .name(name)
                .build();

        String sql = String.format("INSERT INTO %s (uid, name) VALUES (?, ?)", tableName);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, item.getUid().toString());
                ps.setString(2, item.getName());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                item.setUid(ConvertorHelper.String2UUID(keyHolder.getKey().toString()));
            }
            return item;
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistsException(e.getMessage());
        }
    }

    public ProductStatusDto findByUId(UUID uid) {
        String sql = "SELECT * FROM " + tableName + " WHERE uid = ?";
        try {
            ProductStatusDto item = jdbcTemplate.queryForObject(sql, new ProductStatusRowMapper(), uid.toString());
            return item;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("not found product status with uid: %s", uid.toString()));
        }
    }

    public FindAllResult<ProductStatusDto> findAll(int page, int size, String orderField,
            OrderDirection orderDirection) {
        String sanitizedOrderField = allowedOrderFields.containsKey(orderField) ? allowedOrderFields.get(orderField)
                : "uid";
        String orderByClause = String.format(" ORDER BY `%s` %s", sanitizedOrderField,
                (orderDirection != null ? orderDirection.getName() : "ASC"));
        int currentPage = Math.max(1, page);
        int offset = (currentPage - 1) * size;
        if (offset < 0)
            offset = 0;
        String paginationClause = String.format(" LIMIT %d OFFSET %d", size, offset);
        String dataSql = String.format("SELECT * FROM %s%s%s", tableName, orderByClause, paginationClause);
        List<ProductStatusDto> data = jdbcTemplate.query(dataSql, new ProductStatusRowMapper());

        String countSql = String.format("SELECT COUNT(*) FROM %s", tableName);
        Long totalItems = jdbcTemplate.queryForObject(countSql, Long.class);
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

        return FindAllResult.<ProductStatusDto>builder()
                .data(data)
                .pagination(paginationInfo)
                .build();
    }

    public int updateName(UUID uid, String name) {
        String sql = String.format("UPDATE %s SET name = ? WHERE uid = ?", tableName);
        try {
            int rowsAffected = jdbcTemplate.update(sql, name, uid.toString());
            return rowsAffected;
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistsException(e.getMessage());
        }
    }

    @Transactional
    public int setDefault(UUID uid) {
        // 1. Kiểm tra xem UID hiện tại đã là mặc định chưa
        String sqlCheckIfAlreadyDefault = String.format("SELECT is_default FROM %s WHERE uid = ?", tableName);
        Integer isCurrentlyDefault = jdbcTemplate.queryForObject(sqlCheckIfAlreadyDefault, Integer.class,
                uid.toString());

        // Nếu isCurrentlyDefault là null (không tìm thấy UID) hoặc đã là 1 (true)
        if (isCurrentlyDefault == null) {
            throw new NotFoundException(String.format("not found product status with uid: %s", uid.toString()));
        }

        if (isCurrentlyDefault == 1) {
            System.out.println("UID: " + uid + " is already the default. No update needed.");
            return 0; // Trả về 0 vì không có hàng nào bị ảnh hưởng thêm
        }

        // Nếu UID chưa phải là default, tiến hành cập nhật
        int totalRowsAffected = 0;

        String sqlSetOthersToFalse = String.format("UPDATE %s SET is_default = ? WHERE uid != ?", tableName);
        int rowsAffectedOthers = jdbcTemplate.update(sqlSetOthersToFalse, 0, uid.toString()); // 0 cho false
        totalRowsAffected += rowsAffectedOthers;

        String sqlSetOneToTrue = String.format("UPDATE %s SET is_default = ? WHERE uid = ?", tableName);
        int rowsAffectedOne = jdbcTemplate.update(sqlSetOneToTrue, 1, uid.toString()); // 1 cho true
        totalRowsAffected += rowsAffectedOne;

        return totalRowsAffected;
    }

    public int deleteByUid(UUID uid) {
        String sql = String.format("DELETE FROM %s WHERE uid = ?", tableName);
        int rowsAffected = jdbcTemplate.update(sql, uid.toString());
        if (rowsAffected == 0) {
            throw new NotFoundException(String.format("brand not found %s", uid.toString()));
        }
        return rowsAffected;
    }

    public boolean nameExists(String name) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE name = ?", tableName);
        Long count = jdbcTemplate.queryForObject(sql, Long.class, name);
        return count != null && count > 0;
    }
}
