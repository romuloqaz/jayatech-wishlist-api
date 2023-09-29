package com.jayatech.wishlist.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;

@Data
@Builder
public class WishListItem {

    @Id
    private String id;

    private Instant createdAt;

    @DBRef
    private Product product;
}
