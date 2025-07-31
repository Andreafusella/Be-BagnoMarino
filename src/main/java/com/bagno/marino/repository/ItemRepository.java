package com.bagno.marino.repository;

import com.bagno.marino.model.category.Category;
import com.bagno.marino.model.item.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends BaseRepository<Item, Long> {

    @Query("SELECT COUNT(i) > 0 FROM Item i WHERE LOWER(i.name) = LOWER(:title)")
    boolean existsByNormalizedTitle(@Param("title") String title);

    List<Item> findAllByCategory_Id(Long categoryId);

    List<Item> findAllByCategory_IdAndAvailableTrueOrderByOrderIndexAsc(Long categoryId);

    int countByAvailableTrue();

    int countByAvailableFalse();

    List<Item> findByCategoryAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(Category category, Integer orderIndex);

    @Query("SELECT MAX(i.orderIndex) FROM Item i WHERE i.category = :category")
    Optional<Integer> findMaxOrderIndexByCategory(@Param("category") Category category);

    List<Item> findByCategoryAndOrderIndexGreaterThanOrderByOrderIndexAsc(Category category, Integer orderIndex);

    List<Item> findAllByCategory_IdOrderByOrderIndexAsc(Long id);

    List<Item> findByCategoryAndOrderIndexBetweenOrderByOrderIndexAsc(Category category, int i, int newIndex);
}
