package com.capgemini.cartservice.service;

import com.capgemini.cartservice.dto.CartDTO;
import com.capgemini.cartservice.dto.ItemDTO;
import com.capgemini.cartservice.entity.Cart;
import com.capgemini.cartservice.entity.Items;
import com.capgemini.cartservice.repository.CartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public CartDTO getCartById(int id) {
        Cart cart = cartRepository.findById(id).orElse(null);  // Fetch Cart by ID
        return cart != null ? convertEntityToDto(cart) : null;  // Convert entity to DTO
    }

    @Override
    public CartDTO updateCart(Cart cart) {
        Cart updatedCart = cartRepository.save(cart);  // Save updated Cart
        return convertEntityToDto(updatedCart);  // Return updated Cart as DTO
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();  // Fetch all Carts
        List<CartDTO> cartDTOList = new ArrayList<>();
        for (Cart cart : carts) {
            cartDTOList.add(convertEntityToDto(cart));  // Convert Cart entity to CartDTO
        }
        return cartDTOList;
    }

    @Override
    public double cartTotal(CartDTO cartDTO) {
        double total = 0;
        for (ItemDTO itemDTO : cartDTO.getItems()) {
            total += itemDTO.getPrice() * itemDTO.getQuantity();  // Calculate total price
        }
        return total;
    }

    @Override
    public CartDTO addCart(CartDTO cartDTO) {
        Cart cart = convertDtoToEntity(cartDTO);  // Convert DTO to entity
        cart.setTotalPrice(cartTotal(cartDTO)); // Set total price based on cartDTO
        
        Cart savedCart = cartRepository.save(cart);  // Save Cart to DB
        return convertEntityToDto(savedCart);  // Return saved Cart as DTO
    }

    @Override
    public void deleteCart(int cartId) {
        cartRepository.deleteById(cartId);  // Delete Cart by ID
    }

    // Method to convert Cart entity to CartDTO
    public static CartDTO convertEntityToDto(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());
        cartDTO.setTotalPrice(cart.getTotalPrice());

        // Convert list of Item entities to ItemDTOs
        List<ItemDTO> itemDTOs = new ArrayList<>();
        for (Items item : cart.getItems()) {
            itemDTOs.add(convertItemToDto(item));  // Map Items to ItemDTO
        }

        cartDTO.setItems(itemDTOs);
        return cartDTO;
    }

    // Helper method to convert Item entity to ItemDTO
    private static ItemDTO convertItemToDto(Items item) {
        if (item == null) {
            return null;
        }

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setProductName(item.getProductName());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setQuantity(item.getQuantity());  // Ensure quantity is included

        return itemDTO;
    }

    // Method to convert CartDTO to Cart entity
    public static Cart convertDtoToEntity(CartDTO cartDTO) {
        if (cartDTO == null) {
            return null;  // Handle null DTO if necessary
        }

        Cart cart = new Cart();
        cart.setCartId(cartDTO.getCartId());
        cart.setTotalPrice(cartDTO.getTotalPrice());

        // Convert list of ItemDTOs to Items entities
        List<Items> items = new ArrayList<>();
        for (ItemDTO itemDTO : cartDTO.getItems()) {
            items.add(convertItemDtoToEntity(itemDTO));  // Map ItemDTO to Items
        }

        cart.setItems(items);
        return cart;
    }

    // Helper method to convert ItemDTO to Items entity
    private static Items convertItemDtoToEntity(ItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }

        Items item = new Items();
        item.setProductName(itemDTO.getProductName());
        item.setPrice(itemDTO.getPrice());
        item.setQuantity(itemDTO.getQuantity());  // Ensure quantity is correctly set

        return item;
    }
}
