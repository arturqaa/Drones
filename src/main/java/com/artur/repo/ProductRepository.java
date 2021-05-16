package com.artur.repo;

import com.artur.entity.Category;
import com.artur.entity.UserOrder;
import com.artur.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<UserOrder> {

    Page<Product> findAllByCategory(Category category, Pageable pageable);

    Page<Product> findAllByTitle(String title, Pageable pageable);
}
