package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    List<ShoppingCartItem> getAllCartItems();
    ShoppingCart addItemToCart(int userId, int productId, int quantity);
    List<ShoppingCartItem> removeItemFromCart(int userId, int productId);
    void updateItemQuantity(int userId, int productId, int quantity);
    void clearCart(int userId);
    double getTotalCartValue(int userId);
    boolean isProductInCart(int userId, int productId);

}
