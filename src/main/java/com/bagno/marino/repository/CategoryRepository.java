package com.bagno.marino.repository;

import com.bagno.marino.model.category.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long>{

    Category findByName(String name);

    boolean existsByName(String name);
}
