package services.product.api.category.repository;

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

import services.product.data.dto.category.CategoryDto;
import services.product.data.dto.category.CategoryRowMapper;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.data.model.Pagination;
import services.product.exception.custome.AlreadyExistsException;
import services.product.exception.custome.NotFoundException;
import services.product.helper.convertor.ConvertorHelper;
import services.product.helper.generator.GeneratorHelper;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final Map<String, String> allowedOrderFields;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = "categories";
        this.allowedOrderFields = Map.of(
                "uid", "uid",
                "name", "name",
                "createAt", "create_at");
    }

    public CategoryDto insert(String name) {
        CategoryDto category = CategoryDto.builder()
                .uid(GeneratorHelper.RandomUUID())
                .name(name)
                .build();

        String sql = String.format("INSERT INTO %s (uid, name) VALUES (?, ?)", tableName);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, category.getUid().toString());
                ps.setString(2, category.getName());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                category.setUid(ConvertorHelper.String2UUID(keyHolder.getKey().toString()));
            }
            return category;
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistsException(e.getMessage());
        }
    }

    public CategoryDto findByUId(UUID uid) {
        String sql = "SELECT * FROM " + tableName + " WHERE uid = ?";
        try {
            CategoryDto category = jdbcTemplate.queryForObject(sql, new CategoryRowMapper(), uid.toString());
            return category;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("not found category with uid: %s", uid.toString()));
        }
    }

    public FindAllResult<CategoryDto> findAll(int page, int size, String orderField, OrderDirection orderDirection) {
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
        List<CategoryDto> data = jdbcTemplate.query(dataSql, new CategoryRowMapper());

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

        return FindAllResult.<CategoryDto>builder()
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

    public int updatePhotoUrl(UUID uid, String photoUrl) {
        String sql = String.format("UPDATE %s SET photo_url = ? WHERE uid = ?", tableName);
        try {
            int rowsAffected = jdbcTemplate.update(sql, photoUrl, uid.toString());
            return rowsAffected;
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistsException(e.getMessage());
        }
    }

    public int update(UUID uid, String name, String photoUrl) {
        String sql = String.format("UPDATE %s SET name = ?, photo_url = ? WHERE uid = ?", tableName);
        try {
            int rowsAffected = jdbcTemplate.update(sql, name, photoUrl, uid.toString());
            return rowsAffected;
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistsException(e.getMessage());
        }
    }

    public int deleteByUid(UUID uid) {
        String sql = String.format("DELETE FROM %s WHERE uid = ?", tableName);
        int rowsAffected = jdbcTemplate.update(sql, uid.toString());
        if (rowsAffected == 0) {
            throw new NotFoundException(String.format("category not found %s", uid.toString()));
        }
        return rowsAffected;
    }

    public boolean nameExists(String name) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE name = ?", tableName);
        Long count = jdbcTemplate.queryForObject(sql, Long.class, name);
        return count != null && count > 0;
    }

}
