package com.jayatech.wishlist.domain.service;

import com.jayatech.wishlist.domain.model.WishListItem;
import com.jayatech.wishlist.domain.repository.WishListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListItemService {

    @Autowired
    private WishListItemRepository wishListItemRepository;

    public List<WishListItem> findAll() {
        return wishListItemRepository.findAll();
    }
    public WishListItem insert (WishListItem wishlist){
        return wishListItemRepository.save(wishlist);
    }
    public void remove (Long id){
        wishListItemRepository.deleteById(id);
    }
}
