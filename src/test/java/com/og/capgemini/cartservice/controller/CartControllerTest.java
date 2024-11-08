package com.og.capgemini.cartservice.controller;


import com.capgemini.cartservice.CartServiceApplication;
import com.capgemini.cartservice.controller.CartController;
import com.capgemini.cartservice.dto.CartDTO;
import com.capgemini.cartservice.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@ContextConfiguration(classes = CartServiceApplication.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    public void testGetAllCarts() throws Exception {
        // Setup mock CartDTO list
        CartDTO cartDTO1 = new CartDTO();
        cartDTO1.setCartId(1);
        cartDTO1.setTotalPrice(200);

        CartDTO cartDTO2 = new CartDTO();
        cartDTO2.setCartId(2);
        cartDTO2.setTotalPrice(300);

        List<CartDTO> carts = Arrays.asList(cartDTO1, cartDTO2);
        
        when(cartService.getAllCarts()).thenReturn(carts);

        mockMvc.perform(get("/api/cart")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(carts.size()))
                .andExpect(jsonPath("$[0].cartId").value(cartDTO1.getCartId()))
                .andExpect(jsonPath("$[0].totalPrice").value(cartDTO1.getTotalPrice()));
        
        verify(cartService, times(1)).getAllCarts();
    }

    @Test
    public void testGetCartById() throws Exception {
        int cartId = 1;
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cartId);
        cartDTO.setTotalPrice(200);

        when(cartService.getCartById(cartId)).thenReturn(cartDTO);

        mockMvc.perform(get("/api/cart/{id}", cartId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(cartDTO.getCartId()))
                .andExpect(jsonPath("$.totalPrice").value(cartDTO.getTotalPrice()));
        
        verify(cartService, times(1)).getCartById(cartId);
    }

    @Test
    public void testAddCart() throws Exception {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(1);
        cartDTO.setTotalPrice(200);
        cartDTO.setItems(new ArrayList<>()); // Initialize with an empty list to avoid null

        when(cartService.addCart(any(CartDTO.class))).thenReturn(cartDTO);

        mockMvc.perform(post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cartId\": 1, \"totalPrice\": 200, \"items\": []}")) // Ensure items is included in JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartId").value(cartDTO.getCartId()))
                .andExpect(jsonPath("$.totalPrice").value(cartDTO.getTotalPrice()));

        verify(cartService, times(1)).addCart(any(CartDTO.class));
    }


    @Test
    public void testUpdateCart() throws Exception {
        int cartId = 1;
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cartId);
        cartDTO.setTotalPrice(250);

        doNothing().when(cartService).updateCart(any(), eq(cartId));

        mockMvc.perform(put("/api/cart/update/{id}", cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cartId\": 1, \"totalPrice\": 250}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Updated successfully!"));

        verify(cartService, times(1)).updateCart(any(), eq(cartId));
    }

    @Test
    public void testDeleteCart() throws Exception {
        int cartId = 1;

        doNothing().when(cartService).deleteCart(cartId);

        mockMvc.perform(delete("/api/cart/{id}", cartId))
                .andExpect(status().isNoContent());

        verify(cartService, times(1)).deleteCart(cartId);
    }
}
