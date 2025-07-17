package com.bagno.marino.repository;

import com.bagno.marino.model.item.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends BaseRepository<Item, Long> {

    @Query("SELECT COUNT(i) > 0 FROM Item i WHERE LOWER(TRIM(i.title)) = LOWER(TRIM(:title))")
    boolean existsByNormalizedTitle(@Param("title") String title);
}
