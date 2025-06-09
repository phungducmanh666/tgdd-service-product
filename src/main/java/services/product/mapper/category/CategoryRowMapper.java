package services.product.mapper.category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import services.product.data.dto.CategoryDto;

public class CategoryRowMapper implements RowMapper<CategoryDto> {
        @Override
        public CategoryDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            CategoryDto category = new CategoryDto();
            category.setUid(UUID.fromString(rs.getString("uid")));
            category.setName(rs.getString("name"));
            category.setPhotoUrl(rs.getString("photo_url"));
            return category;
        }
    }
