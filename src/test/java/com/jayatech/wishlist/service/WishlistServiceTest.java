package com.jayatech.wishlist.service;

import com.jayatech.wishlist.domain.exception.RegisteredProductException;
import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.exception.WishlistFoundException;
import com.jayatech.wishlist.domain.exception.WishlistMaxSizeException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.WishListItem;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.model.dto.ProductDTO;
import com.jayatech.wishlist.domain.repository.WishlistRepository;
import com.jayatech.wishlist.domain.service.WishlistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @InjectMocks
    private WishlistService wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;

    @Test
    @DisplayName("Should create a wishlist")
    void createWishlist() {
        String id = "wishlistId";
        String userId = "userid1test";
        Wishlist wishlist = Wishlist.builder()
                .id(id)
                .userId(userId)
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.emptyList())
                .build();
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);
        Wishlist savedWishlist = wishlistService.saveWishList(userId);
        assertEquals(wishlist, savedWishlist);
        assertEquals(userId, savedWishlist.getUserId());
        assertEquals(0, savedWishlist.getWishListItems().size());
        assertNotNull(wishlist.getCreatedAt());
    }

    @Test
    @DisplayName("Should throw wishlist found exception")
    void createWishlist_foundException() {
        String userId = "userId1";
        Wishlist savedWishlist = Wishlist.builder()
                .userId(userId)
                .createdAt(Instant.now())
                .wishListItems(Collections.emptyList())
                .build();
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(savedWishlist);
        Wishlist result = wishlistService.saveWishList(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        when(wishlistRepository.findByUserId(userId)).thenReturn(Optional.of(savedWishlist));
        assertThrows(WishlistFoundException.class, () -> wishlistService.saveWishList(userId));
    }

    @Test
    @DisplayName("Should get wishlist by user Id")
    void getWishlistByUserId() {
        String id = "wishlistId";
        String userId = "userid1test";
        Wishlist wishlist = Wishlist.builder()
                .id(id)
                .userId(userId)
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.emptyList())
                .build();
        when(wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));
        Wishlist result = wishlistService.findById(id);
        assertEquals(wishlist, result);
        assertThat(result).usingRecursiveComparison().isEqualTo(wishlist);
        assertEquals(0, result.getWishListItems().size());
        assertEquals(userId, result.getUserId());
    }

    @Test
    @DisplayName("Should throw wishlist not found exception")
    void getWishlistByUserId_notFound() {
        String id = "wishlistId";
        String userId = "userid1test";
        when(wishlistRepository.findById(id)).thenReturn(Optional.empty());
        Optional<Wishlist> wishlist = wishlistRepository.findById(id);
        assertEquals(wishlist, Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> wishlistService.findById(id));
    }


    @Test
    @DisplayName("Should insert a wishlistItem in a wishlist")
    void updateWishList() {
        String userId = "userId";
        Wishlist wishlist = Wishlist.builder()
                .id("wishlistId")
                .userId(userId)
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(new ArrayList<>())
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .id("productId")
                .name("product name")
                .description("product description")
                .price(BigDecimal.valueOf(10.0))
                .build();

        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);
        Wishlist updatedWishlist = wishlistService.updateWishList(wishlist, productDTO);
        verify(wishlistRepository).save(any(Wishlist.class));
        assertEquals(wishlist, updatedWishlist);
        assertEquals(1, updatedWishlist.getWishListItems().size());
        assertEquals(productDTO.getId(), updatedWishlist.getWishListItems().get(0)
                .getProduct().getId());
        assertNotNull(wishlist.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw registered product exception when insert duplicated product")
    void updateWishlist_duplicateProduct() {
        Wishlist wishlist = Wishlist.builder().id("wishlistId").userId("userId").createdAt(Instant.now()).updatedAt(null).wishListItems(Collections.singletonList(WishListItem.builder().id("wishListItemId").createdAt(Instant.now()).product(Product.builder().id("productId").name("product name").description("product description").price(BigDecimal.valueOf(10.0)).build()).build())).build();

        ProductDTO productDTO = ProductDTO.builder()
                .id("productId")
                .name("product name")
                .description("product description")
                .price(BigDecimal.valueOf(10.0))
                .build();
        assertEquals(wishlist.getWishListItems().get(0).getProduct().getId(), productDTO.getId());
        assertThrows(RegisteredProductException.class, () ->
                wishlistService.updateWishList(wishlist, productDTO));
    }

    @Test
    @DisplayName("Should throw wishlist size exception when insert more products than wishlist max size")
    void updateWishList_maxWishlistSize() {
        List<WishListItem> items = new ArrayList<>();
        for (int i = 0; i < WishlistService.WISHLIST_MAX_SIZE; i++) {
            items.add(WishListItem.builder().
                    id(UUID.randomUUID().toString())
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

        ProductDTO productDTO = ProductDTO.builder()
                .id("newProductId")
                .name("new product name")
                .description("new product description")
                .price(BigDecimal.valueOf(20.0))
                .build();
        assertEquals(WishlistService.WISHLIST_MAX_SIZE, wishlist.getWishListItems().size());
        assertThrows(WishlistMaxSizeException.class, () ->
                wishlistService.updateWishList(wishlist, productDTO));
    }

    @Test
    @DisplayName("Should delete a wishlistItem")
    void deleteWishlistItem() {
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

        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);
        wishlistService.removeWishListProduct(wishlist, wishlistItemId);
        assertTrue(wishlist.getWishListItems().isEmpty());
        assertNotNull(wishlist.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw wishlistItem not found when deleting item without being in a list")
    void removeWishListProduct_notFound() {
        String wishlistId = "wishlistId";
        String wishListItemId = "wishListItemId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId).userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id(wishListItemId)
                        .createdAt(Instant.now())
                        .product(Product.builder()
                                .id("productId")
                                .name("product name")
                                .price(BigDecimal.valueOf(10.0))
                                .description("product description")
                                .build())
                        .build()))
                .build();

        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(wishlist);
        wishlistService.removeWishListProduct(wishlist, wishListItemId);
        assertTrue(wishlist.getWishListItems().isEmpty());
        assertNotNull(wishlist.getUpdatedAt());
        assertThrows(ResourceNotFoundException.class, () ->
                wishlistService.removeWishListProduct(wishlist, "wishListItemId2"));
    }

    @Test
    @DisplayName("Should check the product is on the wishlist")
    void checkProduct() {
        String productId = "productId";
        Product product = Product
                .builder()
                .id(productId)
                .name("product name")
                .price(BigDecimal.valueOf(10.0))
                .description("product description")
                .build();
        Wishlist wishlist = Wishlist.builder()
                .id("wishlistId")
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishListItemId")
                        .createdAt(Instant.now())
                        .product(product)
                        .build()))
                .build();
        ProductCheckResponse result = wishlistService.checkProduct(wishlist, productId);
        ProductCheckResponse productCheckResponse = new ProductCheckResponse(product);
        assertEquals(result, productCheckResponse);
        assertNotNull(result.getProduct());
        assertEquals(productId, result.getProduct().getId());
    }

    @Test
    @DisplayName("Should check the product is not on the wishlist")
    void checkProduct_notFound() {
        String productId = "productId";
        Wishlist wishlist = Wishlist
                .builder()
                .id("wishlistId")
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.emptyList())
                .build();

        ProductCheckResponse result = wishlistService.checkProduct(wishlist, productId);
        ProductCheckResponse productCheckResponse = new ProductCheckResponse();
        assertEquals(result, productCheckResponse);
        assertNull(result.getProduct());

    }

    @Test
    @DisplayName("Should check if has not product in the wishlist")
    void hasProductInWishlist_not_found() {
        String productId = "productId";
        Wishlist wishlist = Wishlist.builder()
                .id("wishlistId")
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null).wishListItems(Collections.emptyList())
                .build();
        Product result = wishlistService.hasProductInWishlist(wishlist, productId);
        assertEquals(result, new Product());
        assertNull(result.getId());
    }

    @Test
    @DisplayName("Should check if has product in the wishlist")
    void hasProductInWishlist() {
        String productId = "productId";
        Product product = Product.builder()
                .id(productId)
                .name("product name")
                .price(BigDecimal.valueOf(10.0))
                .description("product description")
                .build();
        Wishlist wishlist = Wishlist.builder()
                .id("wishlistId")
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishListItemId")
                        .createdAt(Instant.now())
                        .product(product)
                        .build()))
                .build();

        Product result = wishlistService.hasProductInWishlist(wishlist, productId);
        assertEquals(result, product);
        assertNotNull(result.getId());
    }

    @Test
    @DisplayName("Should validate if the products is on the wishlist and wishlist size")
    void validateProduct() {
        Wishlist wishlist = Wishlist.builder()
                .id("wishlistId")
                .userId("userId1")
                .createdAt(Instant.now())
                .updatedAt(null)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("item1")
                        .createdAt(Instant.now())
                        .product(Product.builder()
                                .id("productId1")
                                .name("product name 1")
                                .price(BigDecimal.valueOf(10.0))
                                .description("product description 1")
                                .build())
                        .build()))
                .build();
        assertDoesNotThrow(() -> wishlistService.validateProduct(wishlist, "newProductId"));
        RegisteredProductException registeredProductException = assertThrows(RegisteredProductException.class, () -> wishlistService.validateProduct(wishlist, "productId1"));
        assertEquals(WishlistService.REGISTERED_PRODUCT_EXCEPTION_MESSAGE, registeredProductException.getMessage());
    }

}
