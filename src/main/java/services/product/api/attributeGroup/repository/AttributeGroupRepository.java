package services.product.api.attributeGroup.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import services.product.data.dto.attributeGroup.AttributeGroupDto;
import services.product.data.dto.attributeGroup.AttributeGroupRowMapper;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.data.model.Pagination;
import services.product.exception.custome.AlreadyExistsException;
import services.product.exception.custome.NotFoundException;
import services.product.helper.convertor.ConvertorHelper;
import services.product.helper.generator.GeneratorHelper;

@Repository
public class AttributeGroupRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final Map<String, String> allowedOrderFields;

    public AttributeGroupRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = "attribute_groups";
        this.allowedOrderFields = Map.of(
                "uid", "uid",
                "name", "name",
                "createAt", "create_at");
    }

    public AttributeGroupDto insert(UUID categoryUid, String name) {
        AttributeGroupDto item = AttributeGroupDto.builder()
                .uid(GeneratorHelper.RandomUUID())
                .name(name)
                .categoryUid(categoryUid)
                .build();

        String sql = String.format("INSERT INTO %s (uid, name, category_uid) VALUES (?, ?, ?)", tableName);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, item.getUid().toString());
                ps.setString(2, item.getName());
                ps.setString(3, item.getCategoryUid().toString());
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

    public AttributeGroupDto findByUId(UUID uid) {
        String sql = "SELECT * FROM " + tableName + " WHERE uid = ?";
        try {
            AttributeGroupDto attributeGroup = jdbcTemplate.queryForObject(sql, new AttributeGroupRowMapper(),
                    uid.toString());
            return attributeGroup;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("not found attribute group with uid: %s", uid.toString()));
        }
    }

    public FindAllResult<AttributeGroupDto> findAll(UUID categoryUid, int page, int size, String orderField,
            OrderDirection orderDirection) {
        String sanitizedOrderField = allowedOrderFields.containsKey(orderField) ? allowedOrderFields.get(orderField)
                : "uid";
        String orderByClause = String.format(" ORDER BY `%s` %s", sanitizedOrderField,
                (orderDirection != null ? orderDirection.getName() : "ASC"));

        StringBuilder whereClauseBuilder = new StringBuilder();
        List<String> params = new ArrayList<>();

        if (categoryUid != null) {
            whereClauseBuilder.append(" WHERE category_uid = ?");
            params.add(categoryUid.toString());
        }

        String whereClause = whereClauseBuilder.toString();

        int currentPage = Math.max(1, page);
        int offset = (currentPage - 1) * size;
        if (offset < 0)
            offset = 0;
        String paginationClause = String.format(" LIMIT %d OFFSET %d", size, offset);
        String dataSql = String.format("SELECT * FROM %s%s%s%s", tableName, whereClause, orderByClause,
                paginationClause);

        System.out.println(String.format("\n\n %s \n\n", dataSql));

        List<AttributeGroupDto> data = jdbcTemplate.query(dataSql, new AttributeGroupRowMapper(), params.toArray());

        String countSql = String.format("SELECT COUNT(*) FROM %s%s", tableName, whereClause);
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

        return FindAllResult.<AttributeGroupDto>builder()
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

    public int deleteByUid(UUID uid) {
        String sql = String.format("DELETE FROM %s WHERE uid = ?", tableName);
        int rowsAffected = jdbcTemplate.update(sql, uid.toString());
        if (rowsAffected == 0) {
            throw new NotFoundException(String.format("attribute group not found %s", uid.toString()));
        }
        return rowsAffected;
    }

    public boolean nameExists(String name) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE name = ?", tableName);
        Long count = jdbcTemplate.queryForObject(sql, Long.class, name);
        return count != null && count > 0;
    }
}
