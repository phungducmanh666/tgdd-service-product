package services.product.data.dto.variantAttribute;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

public class VariantAttributeRowMapper implements RowMapper<VariantAttributeDto> {
    @Override
    public VariantAttributeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        VariantAttributeDto item = new VariantAttributeDto();
        item.setUid(UUID.fromString(rs.getString("uid")));
        item.setName(rs.getString("name"));
        item.setCreateAt(rs.getDate("create_at"));
        return item;
    }
}
