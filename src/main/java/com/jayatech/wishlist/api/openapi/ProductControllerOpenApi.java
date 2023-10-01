package com.jayatech.wishlist.api.openapi;

import com.jayatech.wishlist.domain.exception.StandardError;
import com.jayatech.wishlist.domain.model.Product;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@OpenAPIDefinition(info=@Info(title="Jayatech Wishlist API"))
@Tag(name = "Products", description = "Api to view the Products")
public interface ProductControllerOpenApi {

    @Operation(summary = "List of products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all products",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))})
    })
     ResponseEntity<List<Product>> getAllProducts();

    @Operation(summary = "Product by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardError.class))})
    })
    @GetMapping("/{productId}")
     ResponseEntity<Product> getProductById(
            @Parameter(name = "productId", description = "Identifier of the product", required = true)
            @PathVariable String productId);

}
