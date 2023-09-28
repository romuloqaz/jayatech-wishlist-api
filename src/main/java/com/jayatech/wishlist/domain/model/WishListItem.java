package com.jayatech.wishlist.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;

@Data
@Builder
public class WishListItem {

    @Id
    private String id;

    //TODO include the time with seconds
    private LocalDate updatedAt;

    @DBRef
    private Product product;
}
