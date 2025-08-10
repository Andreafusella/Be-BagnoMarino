package com.bagno.marino.repository;

import com.bagno.marino.model.category.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long>{

    boolean existsByName(String name);

    List<Category> findAllByParentIdOrderByOrderIndexAsc(Long categoryId);

    List<Category> findByParentIsNullOrderByOrderIndexAsc();

    @Query("SELECT MAX(c.orderIndex) FROM Category c")
    Optional<Integer> findMaxOrderIndex();

    List<Category> findByOrderIndexGreaterThanEqualOrderByOrderIndexAsc(Integer orderIndex);

    List<Category> findAllByParentId(Long categoryId);

    List<Category> findByParentAndOrderIndexGreaterThanOrderByOrderIndexAsc(Category parent, Integer orderIndex);

    List<Category> findAllByOrderByOrderIndexAsc();

    Optional<Category> findByNameIgnoreCase(String normalizedName);


    List<Category> findByParentIsNullAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(Integer orderIndex);

    List<Category> findByParentIdAndOrderIndexGreaterThanEqualOrderByOrderIndexAsc(Long id, Integer orderIndex);

    List<Category> findByParentIdOrderByOrderIndexAsc(Long id);

    List<Category> findByParentIsNullAndOrderIndexGreaterThanOrderByOrderIndexAsc(Integer oldIndex);

    List<Category> findByParentIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(Long id, Integer oldIndex);
}
