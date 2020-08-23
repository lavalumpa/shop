package com.shopstuff.shop.item;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer> {

    boolean existsBySeries(String series);

    void deleteBySeries(String series);

    Optional<Item> findBySeries(String series);


}
