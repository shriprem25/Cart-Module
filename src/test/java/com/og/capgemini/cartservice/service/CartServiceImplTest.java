package com.og.capgemini.cartservice.service;


import com.capgemini.cartservice.dto.CartDTO;
import com.capgemini.cartservice.dto.ItemDTO;
import com.capgemini.cartservice.entity.Cart;
import com.capgemini.cartservice.entity.Items;
import com.capgemini.cartservice.exception.CartNotFoundException;
import com.capgemini.cartservice.exception.CartServiceException;
import com.capgemini.cartservice.repository.CartRepository;
import com.capgemini.cartservice.service.CartServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   
    @Test
    void testGetCartById_NotFound() {
        when(cartRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.getCartById(1));
        verify(cartRepository, times(1)).findById(1);
    }

   

    // Test updateCart method
    @Test
    void testUpdateCart_Success() {
        Cart cart = new Cart();
        cart.setCartId(1);
        cart.setTotalPrice(150.0);

        when(cartRepository.existsById(1)).thenReturn(true);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.updateCart(cart, 1);

        verify(cartRepository, times(1)).existsById(1);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testUpdateCart_CartNotFound() {
        Cart cart = new Cart();
        cart.setCartId(1);
        cart.setTotalPrice(150.0);

        when(cartRepository.existsById(1)).thenReturn(false);

        assertThrows(CartServiceException.class, () -> cartService.updateCart(cart, 1));
        verify(cartRepository, times(1)).existsById(1);
    }

    // Test deleteCart method
    @Test
    void testDeleteCart_Success() {
        when(cartRepository.existsById(1)).thenReturn(true);
        doNothing().when(cartRepository).deleteById(1);

        cartService.deleteCart(1);

        verify(cartRepository, times(1)).existsById(1);
        verify(cartRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteCart_NotFound() {
        when(cartRepository.existsById(1)).thenReturn(false);

        assertThrows(CartNotFoundException.class, () -> cartService.deleteCart(1));
        verify(cartRepository, times(1)).existsById(1);
        verify(cartRepository, never()).deleteById(anyInt());
    }

    

    @Test
    void testGetAllCarts_NoCartsAvailable() {
        when(cartRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(CartServiceException.class, () -> cartService.getAllCarts());
        verify(cartRepository, times(1)).findAll();
    }

    // Test cartTotal method
    @Test
    void testCartTotal_Success() {
        CartDTO cartDTO = new CartDTO();
        ItemDTO item1 = new ItemDTO();
        item1.setPrice(50.0);
        item1.setQuantity(2);
        ItemDTO item2 = new ItemDTO();
        item2.setPrice(30.0);
        item2.setQuantity(1);
        cartDTO.setItems(Arrays.asList(item1, item2));

        double total = cartService.cartTotal(cartDTO);

        assertEquals(130.0, total);
    }

    @Test
    void testCartTotal_EmptyCart() {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setItems(new ArrayList<>());

        assertThrows(CartServiceException.class, () -> cartService.cartTotal(cartDTO));
    }
}

