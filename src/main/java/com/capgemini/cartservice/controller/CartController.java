package com.capgemini.cartservice.controller;

import com.capgemini.cartservice.dto.CartDTO;

import com.capgemini.cartservice.dto.ItemDTO;
import com.capgemini.cartservice.entity.Cart;
import com.capgemini.cartservice.entity.Items;
import com.capgemini.cartservice.service.CartService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@Operation(summary = "Display All Cart Items")
    @GetMapping
	public ResponseEntity<List<CartDTO>> getAllCarts() {
		return new ResponseEntity<>(cartService.getAllCarts(), HttpStatus.NOT_FOUND);
	}

	
	@Operation(summary = "Add to Cart")
	@PostMapping("/add")
	public ResponseEntity<CartDTO> addCart(@RequestBody Cart cart) {

		CartDTO convertEntityToDto = convertEntityToDto(cart);

		cartService.addCart(convertEntityToDto);
		return new ResponseEntity<>(convertEntityToDto, HttpStatus.BAD_REQUEST);
	}
    
	@Operation(summary = "Get Cart item by Id")
	@GetMapping("/{id}")
	public ResponseEntity<CartDTO> getCartById(@PathVariable int id) {
		CartDTO cartById = cartService.getCartById(id);

		return new ResponseEntity<>(cartById, HttpStatus.NOT_FOUND);

	}
    
	@Operation(summary = "Update Cart by Id")
	@PutMapping("update/{id}")
	public ResponseEntity<String> updateCart(@RequestBody Cart cart, @PathVariable int id) {
		
		cartService.updateCart(cart,id);
		return new ResponseEntity<>("Updated successfully!", HttpStatus.CREATED);
	}

	@Operation(summary = "Delete Cart item by Id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCart(@PathVariable int id) {
		cartService.deleteCart(id);
		return ResponseEntity.noContent().build();
	}

	// ----------------------------------------

	// Method to convert Cart entity to CartDTO
	public static CartDTO convertEntityToDto(Cart cart) {
		if (cart == null) {
			return null; // or throw exception if you prefer
		}

		CartDTO cartDTO = new CartDTO();
		cartDTO.setCartId(cart.getCartId());
		cartDTO.setTotalPrice(cart.getTotalPrice());

		// Convert list of Item entities to ItemDTOs
		List<ItemDTO> itemDTOs = cart.getItems().stream().map(item -> convertItemToDto(item))
				.collect(Collectors.toList());

		cartDTO.setItems(itemDTOs);

		return cartDTO;
	}

	// Helper method to convert Item entity to ItemDTO
	private static ItemDTO convertItemToDto(Items item) {
		if (item == null) {
			return null; // or throw exception if you prefer
		}

		ItemDTO itemDTO = new ItemDTO();
		itemDTO.setId(item.getId());
		itemDTO.setProductName(item.getProductName());
		itemDTO.setPrice(item.getPrice());
		itemDTO.setQuantity(item.getQuantity());

		return itemDTO;
	}

	public static Cart convertDtoToEntity(CartDTO cartDTO) {
		if (cartDTO == null) {
			return null; // Or throw exception if cartDTO is mandatory
		}

		Cart cart = new Cart();
		cart.setCartId(cartDTO.getCartId());
		cart.setTotalPrice(cartDTO.getTotalPrice());

		// Convert list of ItemDTOs to list of Items entities
		List<Items> items = cartDTO.getItems().stream().map(itemDTO -> convertItemDtoToEntity(itemDTO))
				.collect(Collectors.toList());

		cart.setItems(items);

		return cart;
	}

	// Helper method to convert ItemDTO to Items entity
	private static Items convertItemDtoToEntity(ItemDTO itemDTO) {
		if (itemDTO == null) {
			return null; // Or throw exception if itemDTO is mandatory
		}

		Items item = new Items();
//		item.setId(itemDTO.getId()); // Use itemDTO ID if applicable, otherwise you may generate a new ID
		item.setProductName(itemDTO.getProductName());
		item.setPrice(itemDTO.getPrice());
		item.setQuantity(itemDTO.getQuantity());

		return item;
	}

}
