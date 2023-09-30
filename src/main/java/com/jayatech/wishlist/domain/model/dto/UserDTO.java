package com.jayatech.wishlist.domain.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

}
