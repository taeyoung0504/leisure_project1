package com.project.leisure.taeyoung.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelRequestRepository extends JpaRepository<CancelRequest, Long> {
  
}