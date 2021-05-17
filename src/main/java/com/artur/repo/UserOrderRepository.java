package com.artur.repo;

import com.artur.entity.Account;
import com.artur.entity.Status;
import com.artur.entity.UserOrder;
import com.artur.types.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserOrderRepository extends JpaRepository<UserOrder,Long>, JpaSpecificationExecutor<UserOrder> {

    Optional<UserOrder> findAllByAccountId(Long id);

    Optional<UserOrder> findAllByStatusAndAccount(Status status, Account account);

    Page<UserOrder> findAllByStatus(Status status, Pageable pageable);

}
