package services.product.mapper.attribute;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import services.product.data.dto.AttributeDto;
import services.product.helper.convertor.ConvertorHelper;

public class AttributeRowMapper implements RowMapper<AttributeDto> {
    @Override
    public AttributeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        AttributeDto item = new AttributeDto();
        item.setUid(UUID.fromString(rs.getString("uid")));
        item.setName(rs.getString("name"));
        item.setAttributeGroupUid(ConvertorHelper.String2UUID(rs.getString("attribute_group_uid")));
        item.setCreateAt(rs.getDate("create_at"));
        return item;
    }
}
