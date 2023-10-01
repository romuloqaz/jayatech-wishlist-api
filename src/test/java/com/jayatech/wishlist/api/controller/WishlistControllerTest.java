package com.jayatech.wishlist.api.controller;

import com.jayatech.wishlist.domain.exception.RegisteredProductException;
import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.exception.WishlistMaxSizeException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.WishListItem;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
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
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WishlistControllerTest {

    @InjectMocks
    private WishlistController wishListController;

    @Mock
    private WishlistService wishlistService;

    @Test
    @DisplayName("Should create a wishlist wish status 201")
    void create_wishlist() {
        String id = "wishlistID";
        String userId = "userid1";
        UserDTO userDTO = UserDTO.builder().userId(userId).build();
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
    @DisplayName("Should get product by Id wish status 200")
    void getProductId() {
        String wishlisId = "wishlistId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlisId).userId("userId1")
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
    @DisplayName("Should get product by Id with status 200")
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
    @DisplayName("Should throw a not found exception wish status 404")
    void getProductId_ProductNotFound() {
        String id = "wishlistId";
        when(wishlistService.findById(id)).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> wishListController.getWishList(id));
        verify(wishlistService).findById(id);
    }

    @Test
    @DisplayName("Should check wishlist and throw a not found exception wish status 404")
    void checkWishList_ResponseEqualsService() {
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
                .wishListItems(Collections.singletonList(WishListItem.builder().
                        id("wishListItemId")
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
    @DisplayName("Should check if there is a product in a wishlist with status 200")
    void checkWishList() {
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
    @DisplayName("Should include wishlist item  with status 200")
    void incrementWishList() {
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
                .updatedAt(null).
                wishListItems(Collections.emptyList())
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
        when(wishlistService.updateWishList(wishlist, productId)).thenReturn(wishlistUpdated);
        ResponseEntity<Wishlist> responseEntity = wishListController.incrementWishlist(wishlistId, productId);
        verify(wishlistService).updateWishList(wishlist, productId);
        assertEquals(wishlistUpdated, responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should throw a found exception product not found status 404")
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

        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        when(wishlistService.updateWishList(wishlist, productId)).thenThrow(RegisteredProductException.class);

        assertThrows(RegisteredProductException.class, () -> wishListController.incrementWishlist(wishlistId, productId));
        verify(wishlistService).findById(wishlistId);
        verify(wishlistService).updateWishList(wishlist, productId);
    }

    @Test
    @DisplayName("Should throw a resource product not found exception status 404")
    void incrementWishList_product_not_found() {
        String wishlistId = "wishlistId";
        String productId = "productIdtest";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishListItemId")
                        .createdAt(Instant.now())
                        .product(Product.builder()
                                .id("productId")
                                .name("product name test")
                                .price(BigDecimal.valueOf(11.5))
                                .description("product test description")
                                .build())
                        .build()))
                .build();

        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        when(wishlistService.updateWishList(wishlist, productId)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> wishListController.incrementWishlist(wishlistId, productId));
        verify(wishlistService).findById(wishlistId);
        verify(wishlistService).updateWishList(wishlist, productId);
    }

    @Test
    @DisplayName("Should throw wishlist size exception with status 400")
    void incrementWishList_max_size() {
        String wishlistId = "wishlistId";
        String productId = "productId";
        List<WishListItem> items = new ArrayList<>();
        for (int i = 0; i < WishlistService.WISHLIST_MAX_SIZE; i++) {
            items.add(WishListItem.builder()
                    .id(UUID.randomUUID().toString())
                    .createdAt(Instant.now())
                    .product(Product.builder()
                            .id("productId" + i)
                            .name("product name" + i)
                            .description("product description" + i)
                            .price(BigDecimal.valueOf(10.0 + i))
                            .build())
                    .build());
        }

        Wishlist wishlist = Wishlist.builder()
                .id("wishlistId")
                .userId("userId")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(items)
                .build();

        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        when(wishlistService.updateWishList(wishlist, productId))
                .thenThrow(WishlistMaxSizeException.class);

        assertThrows(WishlistMaxSizeException.class, () ->
                wishListController.incrementWishlist(wishlistId, productId));
        verify(wishlistService).findById(wishlistId);
        verify(wishlistService).updateWishList(wishlist, productId);
    }

    @Test
    @DisplayName("Should delete a wishlist item with status 204")
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
    @DisplayName("Should throw wishlist item not found exception wish status 404")
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
                        .build()).build();
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId(userId)
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(wishListItem))
                .build();

        when(wishlistService.findById(wishlistId)).thenReturn(wishlist);
        doThrow(ResourceNotFoundException.class).when(wishlistService).removeWishListProduct(wishlist, wishlistItemId);
        assertThrows(ResourceNotFoundException.class, () -> wishListController.deleteProductWishList(wishlistId, wishlistItemId));
        verify(wishlistService).findById(wishlistId);
        verify(wishlistService, times(1)).removeWishListProduct(wishlist, wishlistItemId);
    }
}
