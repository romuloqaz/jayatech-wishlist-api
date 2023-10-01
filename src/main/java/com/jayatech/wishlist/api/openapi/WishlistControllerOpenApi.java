package com.jayatech.wishlist.api.openapi;

import com.jayatech.wishlist.domain.exception.StandardError;
import com.jayatech.wishlist.domain.model.Wishlist;
import com.jayatech.wishlist.domain.model.dto.ProductCheckResponse;
import com.jayatech.wishlist.domain.model.dto.UserDTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@OpenAPIDefinition(info=@Info(title="Wishlist API"))
@Tag(name = "Wishlist", description = "Api to manage wishlist")
public interface WishlistControllerOpenApi {

    @Operation(summary = "Create a new Wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Wishlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "400", description = "Wishlist duplicated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}),
            @ApiResponse(responseCode = "400", description = "Internal error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))})
    })
    @PostMapping()
    ResponseEntity<Wishlist> createWishlist(
            @Parameter(name = "user", description = "User DTO to identifier a user", required = true)
            @RequestBody UserDTO user);

    @Operation(summary = "Wishlist by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the wishlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))})
    })
    @GetMapping("/{wishlistId}")
    ResponseEntity<Wishlist> getWishList(
            @Parameter(name = "wishlistId", description = "Identifier of the Wishlist", required = true)
            @PathVariable String wishlistId);

    @Operation(summary = "Check product on a Wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found on a wishlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductCheckResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found on a wishlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductCheckResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Wishlist not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))})
    })
    @GetMapping("/{wishlistId}/check/{productId}")
    ResponseEntity<ProductCheckResponse> checkWishList(
            @Parameter(name = "wishlistId", description = "Identifier of the Wishlist", required = true) @PathVariable String wishlistId,
            @Parameter(name = "productId", description = "Identifier of the Product", required = true) @PathVariable String productId);

    @Operation(summary = "Increment Wishlist with a new Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Include new Product on a Wishlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "400", description = "Wishlist maximum size",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}),
            @ApiResponse(responseCode = "400", description = "Product registered on a wishlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}),
            @ApiResponse(responseCode = "404", description = "Wishlist not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}),
            @ApiResponse(responseCode = "400", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))})

    })
    @PostMapping("/{wishlistId}/items/{productId}")
    ResponseEntity<Wishlist> incrementWishlist(
            @Parameter(name = "wishlistId", description = "Identifier of the Wishlist", required = true) @PathVariable String wishlistId,
            @Parameter(name = "productId", description = "Identifier of the Product", required = true) @PathVariable String productId);

    @Operation(summary = "Remove a Wishlist Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wishlist item deleted"),
            @ApiResponse(responseCode = "404", description = "Wishlist not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}),
            @ApiResponse(responseCode = "404", description = "Wishlist item not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))}),
            @ApiResponse(responseCode = "400", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))})

    })
    @DeleteMapping("{wishlistId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProductWishList(
            @Parameter(name = "wishlistId", description = "Identifier of the Wishlist", required = true) @PathVariable String wishlistId,
            @Parameter(name = "itemId", description = "Identifier of the Wishlist item", required = true) @PathVariable String itemId);
}
