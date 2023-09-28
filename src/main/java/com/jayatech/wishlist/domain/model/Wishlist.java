package com.jayatech.wishlist.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wishlist {

    @Id
    private String id;

    private String userId;

    //TODO include the time with seconds
    private LocalDate updatedAt;

    private List<WishListItem> wishListItems;
}
