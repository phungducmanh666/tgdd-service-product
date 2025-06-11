package services.product.mapper.category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import services.product.data.dto.CategoryDto;

public class CategoryRowMapper implements RowMapper<CategoryDto> {
        @Override
        public CategoryDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            CategoryDto item = new CategoryDto();
            item.setUid(UUID.fromString(rs.getString("uid")));
            item.setName(rs.getString("name"));
            item.setPhotoUrl(rs.getString("photo_url"));
            item.setCreateAt(rs.getDate("create_at"));
            return item;
        }
    }
