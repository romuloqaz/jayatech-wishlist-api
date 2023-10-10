package com.jayatech.wishlist.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;

@Builder
@Getter
public class WishListItem {

    @Id
    private String id;
    private Instant createdAt;
    @DBRef
    private Product product;
}
