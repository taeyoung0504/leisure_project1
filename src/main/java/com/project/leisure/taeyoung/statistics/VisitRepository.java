package com.project.leisure.taeyoung.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository  extends JpaRepository<Visit, Long>{

	
}
