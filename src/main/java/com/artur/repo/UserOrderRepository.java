package com.artur.repo;

import com.artur.entity.UserOrder;
import com.artur.types.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserOrderRepository extends JpaRepository<UserOrder,Long>, JpaSpecificationExecutor<UserOrder> {

    Optional<UserOrder> findAllByAccountId(Long id);

    Optional<UserOrder> findAllByStatusAndAccountId(StatusType statusType, Long id);
}
