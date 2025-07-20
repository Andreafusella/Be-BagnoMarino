package com.bagno.marino.repository;

import com.bagno.marino.model.item.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends BaseRepository<Item, Integer> {

    @Query("SELECT COUNT(i) > 0 FROM Item i WHERE LOWER(TRIM(i.title)) = LOWER(TRIM(:title))")
    boolean existsByNormalizedTitle(@Param("title") String title);

    List<Item> findAllByCategory_Id(Long categoryId);
}
