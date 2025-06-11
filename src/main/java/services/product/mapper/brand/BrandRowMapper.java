package services.product.mapper.brand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import services.product.data.dto.BrandDto;

public class BrandRowMapper implements RowMapper<BrandDto> {
    @Override
    public BrandDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        BrandDto item = new BrandDto();
        item.setUid(UUID.fromString(rs.getString("uid")));
        item.setName(rs.getString("name"));
        item.setPhotoUrl(rs.getString("photo_url"));
        item.setCreateAt(rs.getDate("create_at"));
        return item;
    }
}
