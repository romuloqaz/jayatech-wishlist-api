package com.jayatech.wishlist.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Document(collection = "wishlist")
public class Wishlist {

    @Id
    private Long id;

    private Long userId;

    private LocalDate updatedAt;

    @DBRef
    private List<WishListItem> wishListItems;
}
