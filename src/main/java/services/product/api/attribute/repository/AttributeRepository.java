package services.product.api.attribute.repository;

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

import services.product.data.dto.attribute.AttributeDto;
import services.product.data.dto.attribute.AttributeRowMapper;
import services.product.data.model.FindAllResult;
import services.product.data.model.OrderDirection;
import services.product.data.model.Pagination;
import services.product.exception.custome.AlreadyExistsException;
import services.product.exception.custome.NotFoundException;
import services.product.helper.convertor.ConvertorHelper;
import services.product.helper.generator.GeneratorHelper;

@Repository
public class AttributeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final Map<String, String> allowedOrderFields;

    public AttributeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = "attributes";
        this.allowedOrderFields = Map.of(
                "uid", "uid",
                "name", "name",
                "createAt", "create_at");
    }

    public AttributeDto insert(UUID attributeGroupUid, String name) {
        AttributeDto item = AttributeDto.builder()
                .uid(GeneratorHelper.RandomUUID())
                .name(name)
                .attributeGroupUid(attributeGroupUid)
                .build();

        String sql = String.format("INSERT INTO %s (uid, name, attribute_group_uid) VALUES (?, ?, ?)", tableName);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, item.getUid().toString());
                ps.setString(2, item.getName());
                ps.setString(3, item.getAttributeGroupUid().toString());
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

    public AttributeDto findByUId(UUID uid) {
        String sql = "SELECT * FROM " + tableName + " WHERE uid = ?";
        try {
            AttributeDto attributeGroup = jdbcTemplate.queryForObject(sql, new AttributeRowMapper(),
                    uid.toString());
            return attributeGroup;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("not found attribute with uid: %s", uid.toString()));
        }
    }

    public FindAllResult<AttributeDto> findAll(UUID attributeGroupUid, int page, int size, String orderField,
            OrderDirection orderDirection) {
        String sanitizedOrderField = allowedOrderFields.containsKey(orderField) ? allowedOrderFields.get(orderField)
                : "uid";
        String orderByClause = String.format(" ORDER BY `%s` %s", sanitizedOrderField,
                (orderDirection != null ? orderDirection.getName() : "ASC"));

        StringBuilder whereClauseBuilder = new StringBuilder();
        List<String> params = new ArrayList<>();

        if (attributeGroupUid != null) {
            whereClauseBuilder.append(" WHERE attribute_group_uid = ?");
            params.add(attributeGroupUid.toString());
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

        List<AttributeDto> data = jdbcTemplate.query(dataSql, new AttributeRowMapper(), params.toArray());

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

        return FindAllResult.<AttributeDto>builder()
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
