package com.DigiClassRoom.paymentService.repository;

import com.DigiClassRoom.paymentService.entity.TransactionalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionalDetailsRepository extends JpaRepository<TransactionalDetails,Long> {
}
