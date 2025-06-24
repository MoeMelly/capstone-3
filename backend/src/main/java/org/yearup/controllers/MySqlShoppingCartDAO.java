package org.yearup.controllers;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.mysql.MySqlCategoryDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Component
public class MySqlShoppingCartDAO implements ShoppingCartDao {
    private static final Logger logger = Logger.getLogger(MySqlShoppingCartDAO.class.getName());
    private final DataSource source;


    public MySqlShoppingCartDAO(DataSource source) {
        this.source = source;
    }









    @Override
    public ShoppingCart getByUserId(int userId) { // retrieve shopping cart from a specific user. method parameter handles which user cart to grab. method is just a container for items.
        String sql = """
                SELECT shopping_cart.product_id, shopping_cart.quantity,
                 products.name, products.price, products.category_id, products.description,products.color,
                 products.image_url,products.stock,products.featured FROM shopping_cart sc
                 JOIN products p ON shopping_cart.product_id = products.product_id WHERE shopping_cart.user_id = ?""";
        try (Connection conn = source.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            ShoppingCart cart = new ShoppingCart();



            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
               product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setDescription(rs.getString("description"));
                product.setColor(rs.getString("color"));
                product.setStock(rs.getInt("stock"));
                product.setFeatured(rs.getBoolean("featured"));
                product.setImageUrl(rs.getString("image_url"));

                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(rs.getInt("quantity"));

                cart.add(item);




            }
            return cart;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<ShoppingCartItem> getAllCartItems() {
        List<ShoppingCartItem> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
         try(Connection connection = source.getConnection()) {
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();

             while (rs.next()) {
                 rs.getInt()
             }

         } catch (SQLException e) {
             throw new RuntimeException(e);
         }


        return list;
    }

    @Override
    public ShoppingCart addItemToCart(int userId, int productId, int quantity) {
        return null;
    }

    @Override
    public List<ShoppingCartItem> removeItemFromCart(int userId, int productId) {
        return List.of();
    }

    @Override
    public void updateItemQuantity(int userId, int productId, int quantity) {

    }

    @Override
    public void clearCart(int userId) {

    }

    @Override
    public double getTotalCartValue(int userId) {
        return 0;
    }

    @Override
    public boolean isProductInCart(int userId, int productId) {
        return false;
    }
}




