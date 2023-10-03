package com.jayatech.wishlist.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "wishlists")
public class Wishlist {

    @Id
    private String id;
    private String userId;
    private Instant createdAt;
    private Instant updatedAt;
    private List<WishListItem> wishListItems;
}
