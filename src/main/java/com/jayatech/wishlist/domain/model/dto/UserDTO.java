package com.jayatech.wishlist.domain.model.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String userId;
}
