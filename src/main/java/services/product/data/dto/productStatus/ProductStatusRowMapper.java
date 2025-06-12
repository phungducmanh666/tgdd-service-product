package services.product.data.dto.productStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

public class ProductStatusRowMapper implements RowMapper<ProductStatusDto> {
    @Override
    public ProductStatusDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductStatusDto item = new ProductStatusDto();
        item.setUid(UUID.fromString(rs.getString("uid")));
        item.setName(rs.getString("name"));
        item.setIsDefault(rs.getBoolean("is_default"));
        item.setCreateAt(rs.getDate("create_at"));
        return item;
    }
}
