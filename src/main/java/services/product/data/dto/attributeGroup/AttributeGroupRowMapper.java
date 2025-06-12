package services.product.data.dto.attributeGroup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import services.product.helper.convertor.ConvertorHelper;

public class AttributeGroupRowMapper implements RowMapper<AttributeGroupDto> {
    @Override
    public AttributeGroupDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        AttributeGroupDto item = new AttributeGroupDto();
        item.setUid(UUID.fromString(rs.getString("uid")));
        item.setName(rs.getString("name"));
        item.setCategoryUid(ConvertorHelper.String2UUID(rs.getString("category_uid")));
        item.setCreateAt(rs.getDate("create_at"));
        return item;
    }
}
