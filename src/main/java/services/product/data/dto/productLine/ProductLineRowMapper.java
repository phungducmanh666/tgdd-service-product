package services.product.data.dto.productLine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import services.product.helper.convertor.ConvertorHelper;

public class ProductLineRowMapper implements RowMapper<ProductLineDto> {
    @Override
    public ProductLineDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductLineDto item = new ProductLineDto();
        item.setUid(UUID.fromString(rs.getString("uid")));
        item.setName(rs.getString("name"));
        item.setCategoryUid(ConvertorHelper.String2UUID(rs.getString("category_uid")));
        item.setBrandUid(ConvertorHelper.String2UUID(rs.getString("brand_uid")));
        item.setCreateAt(rs.getDate("create_at"));
        return item;
    }
}
