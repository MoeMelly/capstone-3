package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
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
                SELECT sc.product_id, sc.quantity,
                 p.name, p.price, p.category_id, p.description,p.color,
                 p.image_url,p.stock,p.featured FROM shopping_cart sc
                 JOIN products p ON sc.product_id = p.product_id WHERE sc.user_id = ?""";
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
    public List<ShoppingCartItem> getAllCartItems(int userid) {
        List<ShoppingCartItem> list = new ArrayList<>();
        String sql = """
                SELECT sc.product_id, sc.quantity, p.name, p.price, p.category_id, p.description, p.color,
                p.image_url, p.stock, p.featured FROM shopping_cart sc JOIN products p on sc.product_id = p.product_id WHERE sc.user_id = ?""";
         try(Connection connection = source.getConnection()) {
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();


             while (rs.next()) {
                 Product product = new Product(
                 rs.getInt("product_id"),
                 rs.getString("name"),
                 rs.getBigDecimal("price"),
                 rs.getInt("category_id"),
                 rs.getString("description"),
                 rs.getString("color"),
                 rs.getInt("Stock"),
                 rs.getBoolean("featured"),
                 rs.getString("image_url"));



                 int quantity = rs.getInt("quantity");


                 ShoppingCartItem item = new ShoppingCartItem();
                 item.setQuantity(quantity);
                 item.setProduct(product);
                 list.add(item);




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
    public void removeItemFromCart(int userId, int productId) {
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




