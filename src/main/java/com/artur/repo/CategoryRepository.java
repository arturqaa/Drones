package com.artur.repo;

import com.artur.entity.Category;
import com.artur.entity.Role;
import com.artur.types.CategoryType;
import com.artur.types.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(CategoryType product);
}
