package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;
import java.util.logging.Logger;

// convert this class to a REST controller
// only logged-in users should have access to these actions
@RestController
@PreAuthorize("isAuthenticated()")
public class ShoppingCartController
{
    // a shopping cart requires

    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired //dependency injection-Spring gives object automatically so no need to instate it.
    public ShoppingCartController(ShoppingCartDao dao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = dao;
        this.userDao = userDao;
        this.productDao = productDao;
    }



    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // get the currently logged-in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
           // return (ShoppingCart) shoppingCartDao.getAllCartItems();
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
        return null;
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PutMapping("/products/{productID}")
    public ShoppingCart addToCart(@PathVariable int productId, @RequestParam int quantity, Principal principal) {
        try {
            int userId = getUserId(principal);
            return shoppingCartDao.addItemToCart(userId, productId, quantity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private int getUserId(Principal principal) {
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");

        return user.getId();
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{productId}")
    public void updateCartItem(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {

        try {
            int userId = getUserId(principal);
            shoppingCartDao.updateItemQuantity(userId, productId, item.getQuantity() );
        } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

    }


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

    @DeleteMapping("/products/{productId}")
    public void deleteCartItem(@PathVariable int productId, Principal principal) {

        try {
            int userId = getUserId(principal);
            shoppingCartDao.removeItemFromCart(userId,productId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in cart");
        }
    }

}
