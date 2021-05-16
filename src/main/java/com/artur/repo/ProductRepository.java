package com.artur.repo;

import com.artur.entity.UserOrder;
import com.artur.entity.Product;
import com.artur.types.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<UserOrder> {

    Page<Product> findAllByCategory(CategoryType categoryType, Pageable pageable);

    Page<Product> findAllByTitle(String title, Pageable pageable);
}
