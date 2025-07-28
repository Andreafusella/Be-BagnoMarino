package com.bagno.marino.repository;

import com.bagno.marino.model.category.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long>{

    Category findByName(String name);

    boolean existsByName(String name);

    List<Category> findAllByParentIdOrderByOrderIndexAsc(Long categoryId);

    List<Category> findByParentIsNullOrderByOrderIndexAsc();

    boolean existsByParent_Id(Long parentId);

    List<Category> findByOrderIndexGreaterThanEqualOrderByOrderIndexAsc(Integer orderIndex);

    List<Category> findAllByParentId(Long categoryId);
}
