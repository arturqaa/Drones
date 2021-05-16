package com.artur.repo;

import com.artur.entity.Role;
import com.artur.entity.Status;
import com.artur.types.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository  extends JpaRepository<Status, Long> {

    Optional<Status> findByStatusName(StatusType status);
}
