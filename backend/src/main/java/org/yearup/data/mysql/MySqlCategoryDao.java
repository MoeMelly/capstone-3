package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    private final DataSource source;


    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
        this.source = dataSource;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("name"), rs.getString("description"));
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        {

            return categories;
        }
    }

    @Override
    public Category getById(int CategoryId) {
        if (CategoryId <= 0) {
            return null;
        }
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, CategoryId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Category(rs.getInt("category_id"), rs.getString("name"), rs.getString("description"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot find id with id " + CategoryId, e);
        }


    }

    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.executeUpdate();

            try (ResultSet key = statement.getGeneratedKeys()) {
                if (key.next()) {
                    int id = key.getInt(1);
                    return new Category(id, category.getName(), category.getDescription());
                } else {
                    throw new SQLException("Insert failed: no ID returned");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



    @Override
    public Category update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";

        try(Connection connection = source.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

    statement.setString(1, category.getName());
    statement.setString(2, category.getDescription());
    statement.setInt(3, category.getCategoryId());
    statement.executeUpdate();



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return category;
    }

    @Override
    public void delete(int categoryId) {
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


        private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

            return new Category() {{
                setCategoryId(categoryId);
                setName(name);
                setDescription(description);
            }};
    }
}
