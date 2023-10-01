package com.jayatech.wishlist.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayatech.wishlist.domain.exception.InternalErrorException;
import com.jayatech.wishlist.domain.exception.RegisteredProductException;
import com.jayatech.wishlist.domain.exception.ResourceNotFoundException;
import com.jayatech.wishlist.domain.exception.WishlistMaxSizeException;
import com.jayatech.wishlist.domain.model.Product;
import com.jayatech.wishlist.domain.model.WishListItem;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.model.dto.UserDTO;
import com.jayatech.wishlist.domain.service.WishlistService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WishlistControllerIntegrationTest {

    private static final String URL_WISHLIST = "/wishlist";

    private static final Instant instant = Instant.ofEpochMilli(1632907200000L);

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private WishlistService wishlistService;

    public static String asJsonString(Object request) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectMapper.writeValueAsString(request);

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test
    @SneakyThrows
    void postWishlistException_userIdNull()  {
        UserDTO request = new UserDTO();
        mvc.perform(post(URL_WISHLIST).contentType(MediaType.APPLICATION_JSON).content
                (asJsonString(request.getUserId()))).andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void postWishlistException_internalError(){
        doThrow(new InternalErrorException(InternalErrorException.EXCEPTION_MESSAGE))
                .when(wishlistService).saveWishList("userIdtest");
        UserDTO request = new UserDTO();
        request.setUserId("userIdtest");
        mvc.perform(post(URL_WISHLIST).contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request.getUserId()))).andExpect(jsonPath(
                        "$.error", is(InternalErrorException.EXCEPTION_MESSAGE)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void getWishlist() {
        String wishlistId = "wishlistId";
        String userId = "userid1";
        String wishlistItemId = "wishListItemId";
        Wishlist wishlist = Wishlist.builder().id(wishlistId).userId(userId)
                .createdAt(instant).updatedAt(instant).wishListItems(Collections
                        .singletonList(WishListItem.builder().id(wishlistItemId)
                                .createdAt(instant).product(Product.builder()
                                        .id("productId").name("product name test")
                                        .price(BigDecimal.valueOf(11.5))
                                        .description("product test description")
                                        .build())
                                .build()))
                .build();
        doReturn(wishlist).when(wishlistService).findById(wishlistId);
        mvc.perform(MockMvcRequestBuilders.get(URL_WISHLIST + "/{wishlistId}",
                        wishlistId)).
                andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(wishlistId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", equalTo(userId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", equalTo("2021-09-29T09:20:00Z")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt", equalTo("2021-09-29T09:20:00Z")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].id", equalTo(wishlistItemId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].createdAt", equalTo("2021-09-29T09:20:00Z")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].product.id", equalTo("productId")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].product.name", equalTo("product name test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].product.price", equalTo(11.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].product.description", equalTo("product test description")));
    }

    @Test
    @SneakyThrows
    void getWishList_notFound() {
        String wishlistId = "wishlistId";
        doThrow(new ResourceNotFoundException(ResourceNotFoundException.EXCEPTION_MESSAGE))
                .when(wishlistService).findById("wishlistId");
        mvc.perform(get(URL_WISHLIST + "/{wishlistId}", wishlistId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(ResourceNotFoundException.EXCEPTION_MESSAGE)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void check_products() {
        String wishlistId = "wishlistId";
        String userId = "userid1";
        String wishlistItemId = "wishListItemId";
        String productId = "productId";
        Product product = Product.builder()
                .id(productId)
                .name("product name test")
                .price(BigDecimal.valueOf(11.5))
                .description("product test description")
                .build();

        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId).userId(userId)
                .createdAt(instant)
                .updatedAt(instant)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id(wishlistItemId)
                        .createdAt(instant)
                        .product(product).build()))
                .build();
        doReturn(wishlist).when(wishlistService).findById(wishlistId);
        doReturn(new ProductCheckResponse(product)).when(wishlistService).checkProduct(wishlist, product.getId());
        mvc.perform(MockMvcRequestBuilders.get(URL_WISHLIST + "/{wishlistId}/check/{productId}", wishlistId, productId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hasProductInWishlist", equalTo(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("product.found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.id", equalTo(productId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.name", equalTo("product name test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.price", equalTo(11.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.description", equalTo("product test description")));
    }

    @Test
    @SneakyThrows
    void check_products_not_found() {
        String wishlistId = "wishlistId";
        String productId = "productId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId")
                .createdAt(instant)
                .updatedAt(instant)
                .wishListItems(Collections.emptyList())
                .build();
        doReturn(wishlist).when(wishlistService).findById(wishlistId);
        doReturn(new ProductCheckResponse()).when(wishlistService).checkProduct(wishlist, productId);
        mvc.perform(MockMvcRequestBuilders.get(URL_WISHLIST + "/{wishlistId}/check/{productId}", wishlistId, productId))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[*]", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hasProductInWishlist", equalTo(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("product.not.found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product", equalTo(null)));
    }

    /*
        This test is temporarily disabled so as not
        to create a new wishlist using the test environment.
    */
    @Test
    @Disabled
    void incrementWishlist() throws Exception {
        String wishlistId = "wishlistId";
        String productId = "productId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId")
                .createdAt(instant)
                .updatedAt(instant)
                .wishListItems(Collections.emptyList())
                .build();

        Wishlist updatedWishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId")
                .createdAt(instant)
                .updatedAt(instant)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishlistItemId").createdAt(instant)
                        .product(Product.builder().id("productId")
                                .name("product test")
                                .price(BigDecimal.valueOf(11.5))
                                .description("product test description")
                                .build())
                        .build()))
                .build();

        doReturn(wishlist).when(wishlistService).findById(wishlistId);
        doReturn(updatedWishlist).when(wishlistService).updateWishList(wishlist, productId);
        mvc.perform(post(URL_WISHLIST + "/wishlistId/items/productId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productId)))
                .andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.[*]", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", equalTo(wishlistId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", equalTo("userId")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt", equalTo("2021-09-29T09:20:00Z")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt", equalTo("2021-09-29T09:20:00Z")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].id", equalTo("wishlistItemId")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].createdAt", equalTo("2021-09-29T09:20:00Z")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].product.id", equalTo("productId")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].product.name", equalTo("product test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].product.price", equalTo(11.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.wishListItems[0].product.description", equalTo("product test description")));
    }

    @Test
    @SneakyThrows
    void incrementWishlist_registered_Product()  {
        String wishlistId = "wishlistId";
        String productId = "productId";

        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId")
                .createdAt(instant)
                .updatedAt(instant)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishlistItemId")
                        .createdAt(instant)
                        .product(Product.builder()
                                .id("productId")
                                .name("product test")
                                .price(BigDecimal.valueOf(11.5))
                                .description("product test description")
                                .build())
                        .build()))
                .build();

        doReturn(wishlist).when(wishlistService).findById(wishlistId);
        doThrow(new RegisteredProductException(RegisteredProductException.EXCEPTION_MESSAGE))
                .when(wishlistService).updateWishList(wishlist, productId);
        mvc.perform(post(URL_WISHLIST + "/wishlistId/items/productId")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(productId)))
                .andExpect(jsonPath("$.error", is(RegisteredProductException.EXCEPTION_MESSAGE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void incrementWishlist_product_not_found()  {
        String wishlistId = "wishlistId";
        String productId = "productIdTest";

        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId("userId")
                .createdAt(instant)
                .updatedAt(instant)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishlistItemId")
                        .createdAt(instant)
                        .product(Product.builder()
                                .id("productId")
                                .name("product test")
                                .price(BigDecimal.valueOf(11.5))
                                .description("product test description")
                                .build())
                        .build()))
                .build();

        doReturn(wishlist).when(wishlistService).findById(wishlistId);
        doThrow(new ResourceNotFoundException(ResourceNotFoundException.EXCEPTION_MESSAGE))
                .when(wishlistService).updateWishList(wishlist, productId);
        mvc.perform(post(URL_WISHLIST + "/wishlistId/items/productIdTest")
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(productId)))
                .andExpect(jsonPath("$.error", is(ResourceNotFoundException.EXCEPTION_MESSAGE)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void incrementWishlist_max_size() {
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

        doReturn(wishlist).when(wishlistService).findById(wishlistId);
        doThrow(new WishlistMaxSizeException(WishlistMaxSizeException.EXCEPTION_MESSAGE)).
                when(wishlistService).updateWishList(wishlist, productId);
        mvc.perform(post(URL_WISHLIST + "/wishlistId/items/productId").
                contentType(MediaType.APPLICATION_JSON).content(asJsonString(productId)))
                .andExpect(jsonPath("$.error", is(WishlistMaxSizeException.EXCEPTION_MESSAGE)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @SneakyThrows
    void deleteProduct()  {
        String wishlistId = "wishlistId";
        String userId = "userid1";
        String itemId = "itemId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId(userId)
                .createdAt(instant)
                .updatedAt(instant)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishlistItemId")
                        .createdAt(instant)
                        .product(Product.builder()
                                .id("productId")
                                .name("product test")
                                .price(BigDecimal.valueOf(11.5))
                                .description("product test description")
                                .build())
                        .build()))
                .build();
        doReturn(wishlist).when(wishlistService).findById(wishlistId);
        doNothing().when(wishlistService).removeWishListProduct(wishlist, itemId);
        mvc.perform(delete(URL_WISHLIST + "/{wishlistId}/items/{itemId}", wishlistId, itemId).contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    void deleteProduct_not_found() {
        String wishlistId = "wishlistId";
        String userId = "userid1";
        String itemId = "itemId";
        Wishlist wishlist = Wishlist.builder()
                .id(wishlistId)
                .userId(userId)
                .createdAt(instant)
                .updatedAt(instant)
                .wishListItems(Collections.singletonList(WishListItem.builder()
                        .id("wishlistItemId")
                        .createdAt(instant)
                        .product(Product.builder()
                                .id("productId")
                                .name("product test")
                                .price(BigDecimal.valueOf(11.5))
                                .description("product test description")
                                .build())
                        .build()))
                .build();
        doReturn(wishlist).when(wishlistService).findById(wishlistId);
        doThrow(new ResourceNotFoundException(WishlistMaxSizeException.EXCEPTION_MESSAGE))
                .when(wishlistService).removeWishListProduct(wishlist, itemId);
        mvc.perform(delete(URL_WISHLIST + "/{wishlistId}/items/{itemId}", wishlistId, itemId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is(ResourceNotFoundException.EXCEPTION_MESSAGE)))
                .andExpect(status().isNotFound());
    }
}
