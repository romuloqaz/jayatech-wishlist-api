package com.jayatech.wishlist.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@Document(collection = "wishlist_item")
public class WishListItem {

    @Id
    private Long id;

    private LocalDate updatedAt;

    @DBRef
    private Product product;
}
