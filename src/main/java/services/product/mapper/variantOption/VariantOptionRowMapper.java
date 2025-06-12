package services.product.mapper.variantOption;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import services.product.data.dto.VariantOptionDto;
import services.product.helper.convertor.ConvertorHelper;

public class VariantOptionRowMapper implements RowMapper<VariantOptionDto> {
    @Override
    public VariantOptionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        VariantOptionDto item = new VariantOptionDto();
        item.setUid(UUID.fromString(rs.getString("uid")));
        item.setName(rs.getString("name"));
        item.setVariantAttributeUid(ConvertorHelper.String2UUID(rs.getString("variant_attribute_uid")));
        item.setCreateAt(rs.getDate("create_at"));
        return item;
    }
}
