package com.jayatech.wishlist.controller;

import com.jayatech.wishlist.api.controller.WishlistController;
import com.jayatech.wishlist.domain.exception.RegisteredProductException;
import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.exception.WishlistMaxSizeException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.WishListItem;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.model.dto.ProductDTO;
import com.jayatech.wishlist.domain.model.dto.UserDTO;
import com.jayatech.wishlist.domain.service.WishlistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WishlistControllerTest {

    @InjectMocks
    private WishlistController wishListController;

    @Mock
    private WishlistService wishlistService;

    @Test
    @DisplayName("Should create a wishlist")
    void create_wishlist() {
        String id = "wishlistID";
        String userId = "userid1";
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .build();
        Wishlist wishlist = Wishlist.builder()
                .id(id)
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.emptyList())
                .build();
        when(wishlistService.saveWishList(userId)).thenReturn(wishlist);
        ResponseEntity<Wishlist> responseEntity = wishListController.createWishlist(userDTO);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertSame(wishlist, responseEntity.getBody());
    }


    @Test
    @DisplayName("Should get product by Id")
    void getProductId() {
        String wishlisId = "wishlistId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlisId)
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.emptyList())
                .build();
        when(wishlistService.findById(wishlisId)).thenReturn(wishlist);
        ResponseEntity<Wishlist> responseEntity = wishListController.getWishList(wishlisId);
        verify(wishlistService).findById(wishlisId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertSame(wishlist, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should get product by Id")
    void getProductId_with_item() {
        String wishlisId = "wishlistId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlisId)
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishListItemId")
                        .createdAt(Instant.ofEpochMilli(1000))
                        .product(Product.builder()
                                .id("productId")
                                .name("product name test")
                                .price(BigDecimal.valueOf(11.5))
                                .description("product test description")
                                .build())
                        .build()))
                .build();
        when(wishlistService.findById(wishlisId)).thenReturn(wishlist);
        ResponseEntity<Wishlist> responseEntity = wishListController.getWishList(wishlisId);
        verify(wishlistService).findById(wishlisId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertSame(wishlist, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should throw a not found exception")
    void getProductId_ProductNotFound() {
        String id = "wishlistId";
        when(wishlistService.findById(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () ->
                wishListController.getWishList(id)
        );
        verify(wishlistService).findById(id);
    }

    @Test
    void testCheckWishList_ResponseEqualsService() {
        String wishlistId = "wishlistId";
        String productId = "productId";
        Product product = Product.builder()
                .id(productId)
                .name("product name test")
                .price(BigDecimal.valueOf(11.5))
                .description("product test description")
                .build();
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishListItemId")
                        .createdAt(Instant.now())
                        .product(product)
                        .build()))
                .build();
        ProductCheckResponse expectedResponse = new ProductCheckResponse(product);
        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        when(wishlistService.checkProduct(wishlist, productId)).thenReturn(expectedResponse);
        ResponseEntity<ProductCheckResponse> responseEntity = wishListController.checkWishList(wishlistId, productId);
        verify(wishlistService).checkProduct(wishlist, productId);
        assertEquals(expectedResponse, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testCheckWishList() {
        String wishlistId = "wishlistId";
        String productId = "productId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.emptyList())
                .build();
        ProductCheckResponse expectedResponse = new ProductCheckResponse();
        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        when(wishlistService.checkProduct(wishlist, productId)).thenReturn(expectedResponse);
        ResponseEntity<ProductCheckResponse> responseEntity = wishListController.checkWishList(wishlistId, productId);
        verify(wishlistService).checkProduct(wishlist, productId);
        assertEquals(expectedResponse, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void incrementWishList() {
        String wishlistId = "wishlistId";
        String productId = "productId";

        Product product = Product.builder()
                .id(productId)
                .name("product name test")
                .price(BigDecimal.valueOf(11.5))
                .description("product test description")
                .build();
        ProductDTO productDTO = ProductDTO.builder()
                .id(productId)
                .name("product name test")
                .price(BigDecimal.valueOf(11.5))
                .description("product test description")
                .build();

        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.emptyList())
                .build();

        Wishlist wishlistUpdated = Wishlist.builder()
                .id(wishlistId)
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishListItemId")
                        .createdAt(Instant.now())
                        .product(product)
                        .build()))
                .build();

        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        when(wishlistService.updateWishList(wishlist, productDTO)).thenReturn(wishlistUpdated);
        ResponseEntity<Wishlist> responseEntity = wishListController.incrementWishlist(wishlistId, productDTO);
        verify(wishlistService).updateWishList(wishlist, productDTO);
        assertEquals(wishlistUpdated, responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should throw a not found exception")
    void incrementWishList_product_found() {
        String wishlistId = "wishlistId";
        String productId = "productId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishListItemId")
                        .createdAt(Instant.now())
                        .product(Product.builder()
                                .id(productId)
                                .name("product name test")
                                .price(BigDecimal.valueOf(11.5))
                                .description("product test description")
                                .build())
                        .build()))
                .build();
        ProductDTO productDTO = ProductDTO.builder()
                .id(productId)
                .name("product name test")
                .price(BigDecimal.valueOf(11.5))
                .description("product test description")
                .build();
        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        when(wishlistService.updateWishList(wishlist, productDTO)).thenThrow(RegisteredProductException.class);

        assertThrows(RegisteredProductException.class, () ->
                wishListController.incrementWishlist(wishlistId, productDTO)
        );
        verify(wishlistService).findById(wishlistId);
        verify(wishlistService).updateWishList(wishlist, productDTO);
    }

    @Test
    @DisplayName("Should throw a not found exception")
    void incrementWishList_max_size() {
        String wishlistId = "wishlistId";
        String productId = "productId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .wishListItems(Arrays.asList(WishListItem.builder()
                        .id("wishListItemId1")
                        .createdAt(Instant.now())
                        .product(Product.builder()
                                .id(productId)
                                .name("product name test")
                                .price(BigDecimal.valueOf(11.5))
                                .description("product test description")
                                .build())
                        .build(),
                        WishListItem.builder()
                                .id("wishListItemId2")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId3")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId4")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId5")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId6")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId7")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId8")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId9")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId10")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId11")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId12")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId13")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId14")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId15")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId16")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId17")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId18")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId19")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build(),
                        WishListItem.builder()
                                .id("wishListItemId20")
                                .createdAt(Instant.now())
                                .product(Product.builder()
                                        .id(productId)
                                        .name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build()
                        ))
                .build();
        ProductDTO productDTO = ProductDTO.builder()
                .id(productId)
                .name("product name test 21")
                .price(BigDecimal.valueOf(11.5))
                .description("product test description 2")
                .build();
        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        when(wishlistService.updateWishList(wishlist, productDTO)).thenThrow(WishlistMaxSizeException.class);

        assertThrows(WishlistMaxSizeException.class, () ->
                wishListController.incrementWishlist(wishlistId, productDTO)
        );
        verify(wishlistService).findById(wishlistId);
        verify(wishlistService).updateWishList(wishlist, productDTO);
    }

    @Test
    @DisplayName("Should create a wishlist")
    void deleteProduct() {
        String wishlistId = "wishlistID";
        String userId = "userid1";
        String wishlistItemId = "wishListItemId";
        WishListItem wishListItem = WishListItem.builder()
                .id(wishlistItemId)
                .createdAt(Instant.now())
                .product(Product.builder()
                        .id("productId")
                        .name("product name test")
                        .price(BigDecimal.valueOf(11.5))
                        .description("product test description")
                        .build())
                .build();
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId(userId)
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(wishListItem))
                .build();

        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        doNothing().when(wishlistService).removeWishListProduct(wishlist, wishlistItemId);
        wishListController.deleteProductWishList(wishlistId, wishlistItemId);
        verify(wishlistService, times(1)).removeWishListProduct(wishlist, wishlistItemId);
    }

    @Test
    @DisplayName("Should create a wishlist")
    void deleteProduct_not_found() {
        String wishlistId = "wishlistID";
        String userId = "userid1";
        String wishlistItemId = "wishListItemId";
        WishListItem wishListItem = WishListItem.builder()
                .id(wishlistItemId)
                .createdAt(Instant.now())
                .product(Product.builder()
                        .id("productId")
                        .name("product name test")
                        .price(BigDecimal.valueOf(11.5))
                        .description("product test description")
                        .build())
                .build();
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId(userId)
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(wishListItem))
                .build();

        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        doThrow(ResourceNotFoundException.class).when(wishlistService).removeWishListProduct(wishlist, wishlistItemId);
        assertThrows(ResourceNotFoundException.class, () ->
            wishListController.deleteProductWishList(wishlistId, wishlistItemId)
            );
        verify(wishlistService).findById(wishlistId);
        verify(wishlistService, times(1)).removeWishListProduct(wishlist, wishlistItemId);
    }
}
