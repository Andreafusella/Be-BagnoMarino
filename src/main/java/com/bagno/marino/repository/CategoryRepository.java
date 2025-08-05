package com.bagno.marino.repository;

import com.bagno.marino.model.category.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long>{

    Category findByName(String name);

    boolean existsByName(String name);

    List<Category> findAllByParentIdOrderByOrderIndexAsc(Long categoryId);

    List<Category> findByParentIsNullOrderByOrderIndexAsc();

    boolean existsByParent_Id(Long parentId);

    @Query("SELECT MAX(c.orderIndex) FROM Category c")
    Optional<Integer> findMaxOrderIndex();

    List<Category> findByOrderIndexGreaterThanEqualOrderByOrderIndexAsc(Integer orderIndex);

    List<Category> findAllByParentId(Long categoryId);

    List<Category> findByParentAndOrderIndexGreaterThanOrderByOrderIndexAsc(Category parent, Integer orderIndex);

    List<Category> findAllByOrderByOrderIndexAsc();

    Category findByParentId(Long subCategoryId);

    List<Category> findAllByParentIdIsNullOrderByOrderIndexAsc();

    Optional<Category> findByNameIgnoreCase(String normalizedName);
}
