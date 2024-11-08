package com.capgemini.cartservice.service;

import com.capgemini.cartservice.dto.CartDTO;
import com.capgemini.cartservice.entity.Cart;

import java.util.List;

public interface CartService {
    CartDTO getCartById(int id);
    CartDTO updateCart(Cart cart);
    List<CartDTO> getAllCarts();
    double cartTotal(CartDTO cartDTO);
    CartDTO addCart(CartDTO cartDTO);
    void deleteCart(int cartId);
}











//package com.capgemini.cartservice.service;
//
//import com.capgemini.cartservice.entity.Cart;
//import java.util.List;
//
//public interface CartService {
//    Cart getCartById(int id);
//    Cart updateCart(Cart cart);
//    List<Cart> getAllCarts();
//    double cartTotal(Cart cart);
//    Cart addCart(Cart cart);
//    void deleteCart(int cartId);
//}

